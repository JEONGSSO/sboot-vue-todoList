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
@RequestMapping("board")
public class BoardController {
   
   @Autowired
   private BoardMapper boardMapper;

   @GetMapping("/list")
   public List<BoardVo> getboard() {
      List<BoardVo> boardList = new ArrayList<>();
      boardList = boardMapper.listAll();
      return boardList;
   }

   @GetMapping("/count")
   public int boardPageCount() {
      int totalCount = boardMapper.totalCount();
      return totalCount;
   }

   @GetMapping("/page/{perPage}")
   public List<BoardVo> listPage(@PathVariable int perPage) {
      if (perPage <= 0) {
         perPage = 1;
      }
      perPage = (perPage - 1) * 5;
      List<BoardVo> boardListPage = new ArrayList<>();
      boardListPage = boardMapper.listPage(perPage);
      return boardListPage;
   }

   @PostMapping("")
   public BoardVo regist(@RequestBody BoardVo boardvo) {
      boardMapper.regist(boardvo);
      return boardvo;
   }

   @GetMapping("/bno/{bno}")
   public BoardVo readBoard(@PathVariable int bno) {
      BoardVo read = boardMapper.readBoard(bno);
      return read;
   }
   
   @PatchMapping("/bno/{bno}")
   public void updateBoard(@RequestBody BoardVo boardvo) {
      boardMapper.updateBoard(boardvo);
   }

   @DeleteMapping("/bno/{bno}")
   public void deleteBoard(@PathVariable int bno) {
      boardMapper.deleteBoard(bno);
   }
}