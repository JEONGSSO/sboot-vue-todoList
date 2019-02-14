package com.test.mappers;

import java.util.List;

import com.test.Vo.BoardVo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BoardMapper {

   @Select("select * from board")
   public List<BoardVo> listAll();

   @Insert ("insert into board(title, content, writer)values( #{title}, #{content}, #{writer})")
   public void regist(BoardVo boardvo);

   @Select("select * from board where bno=#{bno}")
   public BoardVo readBoard(@Param("bno") int bno);
}