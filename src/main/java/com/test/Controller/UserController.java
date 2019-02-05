package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.todoVo;
import com.test.mappers.TestMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

   @Autowired
   private TestMapper testmapper;

	@PostMapping("regist")
	public ResponseEntity<String> regist(@RequestBody todoVo todo) {
      try {
         testmapper.regist(todo);
			return new ResponseEntity<>("", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
   }

   @GetMapping("list")
   public List<todoVo> todoList() {
      List<todoVo> todolist = new ArrayList<>();
      todolist = testmapper.listAll();
      return todolist;
   }
   
}