package com.housaire;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.housaire.dal.domain.SendLog;
import com.housaire.dal.mapper.SendLogMapper;
import com.housaire.security.crypto.CryptoUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
    public static void main( String[] args ) throws IllegalAccessException {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        SendLogMapper mapper = applicationContext.getBean(SendLogMapper.class);
        SendLog log = new SendLog();
        log.setMobile("15268830372");
        log.setStatus(1);
        log.setAppid("test");
        log.setCreatetime(new Date());
        log.setSupplier("crypto_test");
        log.setTemplateCode("SMS_TEST");


//        System.out.println(mapper.insert(log));
//
//        System.err.println(mapper.selectById(log.getId()));

//        Map<String, Object> queryMaps = new HashMap<>();
//        queryMaps.put("mobile", CryptoUtils.encryptForAES("15268830372", "enc"));
//        queryMaps.put("status", 1);
//        System.out.println(mapper.selectByMap(queryMaps));

        System.out.println(mapper.selectSendLogs("15268830372", 1));

        /*QueryWrapper<SendLog> query = new QueryWrapper<>();
        query.eq("mobile", "15268830372");
        query.eq("status", 1);
        query.orderByDesc("id");
        query.last(" limit 6");
        System.out.println(mapper.selectList(query));
        for (int i = 0; i < query.getExpression().getNormal().size(); ) {
            if (query.getExpression().getNormal().get(i) instanceof SqlKeyword) {
                i++;
                continue;
            }
            ISqlSegment columnName = query.getExpression().getNormal().get(i + 0);
            ISqlSegment columnValue = query.getExpression().getNormal().get(i + 2);
            Field columnNameArg$2 = FieldUtils.getAllFields(columnName.getClass())[1];
            columnNameArg$2.setAccessible(true);
            Field columnValueArg$2 = FieldUtils.getAllFields(columnValue.getClass())[1];
            columnValueArg$2.setAccessible(true);
            System.out.println(columnNameArg$2.get(columnName) + " = " + columnValueArg$2.get(columnValue));
            i += 3;
        }*/
    }
}
