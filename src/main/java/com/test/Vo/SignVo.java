package com.test.Vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude={"upw"})
public class SignVo {

   private String uid;
   private String upw;

}