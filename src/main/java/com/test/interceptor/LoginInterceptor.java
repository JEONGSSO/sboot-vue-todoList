package com.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.mappers.SignMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter implements SessionKey{

   private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

   @Autowired
   private SignMapper signMapper;

   @Override
   public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {

      logger.info("========================== login pre");

      // HttpSession session = req.getSession();

      // if (session.getAttribute(LOGIN) != null) { //obj라 not equal null 해줘야한다.
      //    session.removeAttribute(LOGIN); //로그인한 객체가 있으면 지운다.
      // }

      return true;

   }
}