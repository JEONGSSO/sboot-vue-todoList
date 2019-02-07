package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Null;

import com.test.Vo.todoVo;
import com.test.mappers.TestMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class UserController {

   @Autowired
   private TestMapper testmapper;
   
   @GetMapping("list")
   public List<todoVo> todoList() {
      List<todoVo> todolist = new ArrayList<>();
      todolist = testmapper.listAll();
      return todolist;
   }

	@PostMapping("regist")
	public ResponseEntity<todoVo> regist(@RequestBody todoVo todo) {
      try {
         // System.out.println(todo);
         testmapper.regist(todo);
			return new ResponseEntity<>(todo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
   }

   @DeleteMapping("delete/{id}")
   public ResponseEntity<Null> delete(@PathVariable int id) {
      try {
         // System.out.println(id);
         testmapper.remove(id);
         return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }
   
}