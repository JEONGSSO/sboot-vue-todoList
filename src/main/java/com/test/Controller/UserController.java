package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.ResponseVo;
import com.test.Vo.UserVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

   @GetMapping
   public ResponseVo<?> getUsers() {
      ResponseVo<List<UserVO>> resp = new ResponseVo<>();

      List<UserVO> list = new ArrayList<>();
      // list.add(new UserVO("테스트", "010"));
      // list.add(new UserVO("테", "0102323"));

      resp.setResponse(list);
      return resp;

   }
}