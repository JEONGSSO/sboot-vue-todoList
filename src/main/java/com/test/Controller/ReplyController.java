package com.test.Controller;

import java.util.ArrayList;
import java.util.List;

import com.test.Vo.ReplyVo;
import com.test.mappers.ReplyMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("reply/")
public class ReplyController {

   @Autowired
   private ReplyMapper replymapper;

   @GetMapping("{bno}")
   public List<ReplyVo> getReply(@PathVariable int bno) {
      List<ReplyVo> replyall = new ArrayList<>();
      replyall = replymapper.getReplyAll(bno);
      return replyall;
   }
   
}