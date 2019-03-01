package com.test.Vo;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyVo {

   private int rno;
   private int bno;
   private String rtext;
   private String replyer;
   private Date rdate;
   private Boolean isEdit;
}
