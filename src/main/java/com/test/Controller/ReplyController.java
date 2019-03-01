package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.ReplyVo;
import com.test.mappers.ReplyMapper;

import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("reply/")
public class ReplyController {

   @Autowired
   private ReplyMapper replyMapper;

   @GetMapping("{bno}")
   public List<ReplyVo> getReply(@PathVariable int bno) {
      List<ReplyVo> replyall = new ArrayList<>();
      replyall = replyMapper.getReplyAll(bno);
      return replyall;
   }
   @PostMapping("add/{bno}")
   public ResponseEntity<Integer> addReply(@PathVariable int bno, ReplyVo replyvo) {
      try {
         replyMapper.addReply(bno, replyvo);
         return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }

   @PatchMapping("edit")
   public ResponseEntity<Null> replyEdit(@RequestBody ReplyVo replyvo) {
      try {
         replyMapper.replyEdit(replyvo);
         return new ResponseEntity<>(HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }

   @DeleteMapping("del/{bno}/{rno}")
   public void replyDel(@PathVariable int bno, @PathVariable int rno) {
      replyMapper.replyDel(bno, rno);
   }
   
}