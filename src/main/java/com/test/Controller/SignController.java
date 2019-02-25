package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.SignVo;
import com.test.mappers.SignMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("sign")
public class SignController {

   @Autowired
   private SignMapper signmapper;

   @GetMapping("")
   public ResponseEntity<List<SignVo>> login() {
      try {
         List<SignVo> login = new ArrayList<>();
         System.out.println(login);
         signmapper.login(login);
         return new ResponseEntity<>(login, HttpStatus.OK);
         
      } catch (Exception e) {
         System.out.println(e);
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }

   @PostMapping("")
   public ResponseEntity<SignVo> signUp(@RequestBody SignVo signvo) {
      try {
         signmapper.signUp(signvo);
         return new ResponseEntity<>(signvo, HttpStatus.OK);
      } catch (Exception e) {
         System.out.println(e);
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }

}