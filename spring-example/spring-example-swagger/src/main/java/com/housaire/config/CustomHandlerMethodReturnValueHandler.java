package com.housaire.config;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

public class CustomHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler
{

    @Override
    public boolean supportsReturnType(MethodParameter returnType)
    {
        if(returnType.getMethod().getReturnType().isAssignableFrom(ResponseEntity.class)) {
            return false;
        }
        Class<?> controllerClass = returnType.getContainingClass();
        return !returnType.hasMethodAnnotation(Raw.class) &&
                (controllerClass.isAnnotationPresent(RestController.class) ||
                        controllerClass.isAnnotationPresent(ResponseBody.class) ||
                        returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception
    {
        ResponseBaseDto responseDto;
        if (returnValue instanceof ResponseBaseDto)
        {
            responseDto = (ResponseBaseDto) returnValue;
        }
        else
        {
            responseDto = ResponseBaseDto.success(returnValue);
        }

        // 标识请求是否已经在该方法内完成处理
        modelAndViewContainer.setRequestHandled(true);

        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.getWriter().write(JSON.toJSONString(responseDto));
    }

}
