package com.test.Vo;

import lombok.Data;

@Data
// @ToString(exclude={"pw"})
public class TodoVo {
   private int id;
   private String content;

}