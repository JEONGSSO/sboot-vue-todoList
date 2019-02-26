package com.test.mappers;

import com.test.Vo.SignVo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface SignMapper {

   @Select("select count(*) from user where uid = #{uid} and upw = #{upw}")
   public int login(SignVo signvo);
   
   @Insert("insert into user values( #{uid}, #{upw})")
   public void signUp(SignVo signvo);

   // @Select("select * from user where id = #{id} pw = #{pw}")
   // public List<SignVo> login(@Param("id") String id, @Param("pw") String pw);
}