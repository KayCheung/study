package com.housaire.sign;

import com.housaire.sign.annotation.SignValidated;
import com.housaire.sign.utils.SpringContextHolder;
import com.housaire.sign.validator.SignValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/12
 */
@Slf4j
public class SignInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 校验签名
        if (handlerMethod.getMethod().isAnnotationPresent(SignValidated.class)) {
            log.info("接口签名校验, 访问地址：{}, 请求参数为：{}", request.getRequestURI(), request.getParameterMap());
            SignValidated signValidated = handlerMethod.getMethod().getAnnotation(SignValidated.class);
            SignValidator validator = SpringContextHolder.getBean(signValidated.value());
            SignContext signContext = validator.getSignContext(request.getParameterMap());
            if(!validator.validate(signContext)) {
                log.error("签名校验不通过, 访问地址：{}, 请求参数为：{}", request.getRequestURI(), request.getParameterMap());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; Charset=UTF-8");
                response.getWriter().write("签名校验不通过");
                return false;
            }
        }
        return true;
    }

}
