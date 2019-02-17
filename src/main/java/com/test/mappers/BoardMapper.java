package com.test.mappers;

import java.util.List;

import com.test.Vo.BoardVo;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BoardMapper {

   @Select("select * from board")
   public List<BoardVo> listAll();

   @Insert ("insert into board(title, content, writer)values( #{title}, #{content}, #{writer})")
   public void regist(BoardVo boardvo);

   @Select("select * from board where bno = #{bno}")
   public BoardVo readBoard(@Param("bno") int bno);

   @Update("update board set title = #{title},content = #{content}, regdata = now() where bno = #{bno}")
   public void updateBoard(BoardVo boardvo);
   // 500Error 리턴타입 문제.

   @Delete("delete from board where bno = #{bno}")//sdsdsdsd
   public void deleteBoard(@Param("bno") int bno);

}