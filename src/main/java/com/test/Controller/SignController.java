package com.test.Controller;

import com.test.Vo.SignVo;
import com.test.mappers.SignMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("sign")
public class SignController {

   @Autowired
   private SignMapper signMapper;

   @PostMapping("/login")
   public int login(@RequestBody SignVo signvo) {
      int login = signMapper.login(signvo);
      return login;
         
   }

   @PostMapping("")
   public ResponseEntity<String> signUp(@RequestBody SignVo signvo) {
      try {
         // signMapper.signUp(signvo); Todo
         return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }


}