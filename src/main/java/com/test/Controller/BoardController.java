package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.BoardVo;
import com.test.mappers.BoardMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class BoardController {
   
   @Autowired
   private BoardMapper boardMapper;

   @GetMapping("list")
   public List<BoardVo> getboard() {
      List<BoardVo> boardList = new ArrayList<>();
      boardList = boardMapper.listAll();
      return boardList;
   }

   @PostMapping("regist")
   public BoardVo regist(@RequestBody BoardVo boardvo) {
      boardMapper.regist(boardvo);
      return boardvo;
   }

   @GetMapping("read/{bno}")
   public BoardVo readBoard(@PathVariable int bno) {
      BoardVo read = boardMapper.readBoard(bno);
      return read;
   }
   
   @PatchMapping("update")
   public void updateBoard(@RequestBody BoardVo boardvo) {
      boardMapper.updateBoard(boardvo);
   }

   @DeleteMapping("delete/{bno}")
   public void deleteBoard(@PathVariable int bno) {
      boardMapper.deleteBoard(bno);
   }
}