package com.test.mappers;

import java.util.List;

import com.test.Vo.SignVo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SignMapper {

   // @Select("select * from user where id = #{id} pw = #{pw}")
   // public List<SignVo> login(@Param("id") String id, @Param("pw") String pw);
   
   @Select("select * from user where id = #{uid} and pw = password(#{upw})")
   public List<SignVo> login(List<SignVo> signvo);

   @Insert("insert into user values( #{id}, #{pw})")
   public SignVo signUp(SignVo signvo);
}