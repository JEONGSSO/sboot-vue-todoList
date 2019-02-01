package com.test.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test/")
public class UserController {

   @GetMapping("ss")
   public void getMethodName() {
      System.out.println("testststststt");
   }
}