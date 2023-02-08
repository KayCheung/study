package com.housaire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/18
 */
@RequestMapping("/open")
@RestController
public class AccountOpenController {


    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(HttpServletRequest request) {
        return request.getQueryString();
    }

}
