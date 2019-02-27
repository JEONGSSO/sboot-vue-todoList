package com.test.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class loginConfig implements WebMvcConfigurer{

   @Autowired
   @Qualifier(value = "loginInter")
   private HandlerInterceptor interceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(interceptor)
                     .addPathPatterns("/**")
                     .excludePathPatterns("/sign/**");
   }
}