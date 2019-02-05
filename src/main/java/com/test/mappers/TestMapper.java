package com.test.mappers;

import java.util.List;

import com.test.Vo.todoVo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface TestMapper {

   @Insert("insert into todo(content) values( #{content} )")
   public void regist(todoVo todo);
   
   // @Delete("delete from user where bno=#{bno}")
   // public void remove(@Param("bno") int bno);

   // @Select("select * from board where bno=#{bno}")
   // public todoVo search(@Param("bno") int bno);

   @Select("select * from todo")
   public List<todoVo> listAll();



      
   
}