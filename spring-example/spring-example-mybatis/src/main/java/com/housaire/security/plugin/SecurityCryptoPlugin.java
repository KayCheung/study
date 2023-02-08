package com.housaire.security.plugin;

import com.housaire.security.crypto.CryptoUtils;
import com.housaire.security.utils.ReflectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/8/18
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SecurityCryptoPlugin implements Interceptor {

    private static ConcurrentMap<String, MapperConfig> mapperConfigs = new ConcurrentHashMap<>();
    private final static Lock lock = new ReentrantLock();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        MapperConfig mapperConfig = getMapperConfig(ms.getId());

        if (Objects.nonNull(invocation.getArgs()[1]) && Objects.nonNull(mapperConfig)) {
            // 获取方法参数列表
            Parameter[] parameters = mapperConfig.getMethod().getParameters();
            if (ArrayUtils.isNotEmpty(parameters)) {
                encrypt(invocation.getArgs(), parameters);
            }
        }

        // 执行statement
        Object result = invocation.proceed();

        // 如果是select，需要处理返回结果（对数据进行解密）
        if (ms.getSqlCommandType() == SqlCommandType.SELECT && Objects.nonNull(mapperConfig)) {
            Class<?> returnType = mapperConfig.getMethod().getReturnType();
            CryptoUtils.decrypt(result, returnType);
        }
        return result;
    }

    private void encrypt(Object[] args, Parameter[] parameters) {
        // 解析@Param参数
        if (args[1] instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) args[1];
            for (Parameter parameter : parameters) {
                Param param = parameter.getAnnotation(Param.class);
                if (null != param) {
                    Object originValue = paramMap.get(param.value());
                    Object encryptValue = CryptoUtils.encrypt(originValue, parameter);
                    paramMap.put(param.value(), encryptValue);
                }
            }
        } else if (ReflectionUtils.isPrimitive(args[1].getClass()) || args[1] instanceof String) {
            args[1] = CryptoUtils.encrypt(args[1], parameters[0]);
        } else if (ReflectionUtils.isReferenceType(args[1].getClass())) {
            CryptoUtils.encrypt(args[1], parameters[0]);
        }
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private MapperConfig getMapperConfig(String msId) {
        if (mapperConfigs.containsKey(msId)) {
            return mapperConfigs.get(msId);
        }
        MapperConfig mapperConfig;
        lock.lock();
        try {
            if (mapperConfigs.containsKey(msId)) {
                return mapperConfigs.get(msId);
            }
            mapperConfig = new MapperConfig();
            mapperConfig.setId(msId);
            int lastDotIndex = msId.lastIndexOf(".");

            // 获取mapper
            if (msId.indexOf(".") == -1) {
                throw new IllegalArgumentException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
            } else {
                String mapperClassStr = msId.substring(0, lastDotIndex);
                try {
                    mapperConfig.setMapperClass(Class.forName(mapperClassStr));
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }

            // 获取entity
            Type[] types = mapperConfig.getMapperClass().getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType t = (ParameterizedType) type;
                    if (null != t.getActualTypeArguments() && t.getActualTypeArguments().length > 0) {
                        Class<?> entityClass = (Class<?>) t.getActualTypeArguments()[0];
                        mapperConfig.setEntityClass(entityClass);
                        break;
                    }
                }
            }

            // 获取方法
            String methodName = msId.substring(lastDotIndex + 1);
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(mapperConfig.getMapperClass());
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    mapperConfig.setMethod(method);
                    break;
                }
            }

            if (Objects.isNull(mapperConfig.getMethod())) {
                throw new RuntimeException("未找到对应的方法: " + methodName);
            }

            mapperConfigs.putIfAbsent(msId, mapperConfig);
        } finally {
            lock.unlock();
        }
        return mapperConfig;
    }

    @Data
    private static class MapperConfig {

        private String id;

        private Method method;

        private Class<?> entityClass;

        private Class<?> mapperClass;

    }

}
