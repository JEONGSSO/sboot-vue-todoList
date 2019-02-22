package com.test.mappers;

import java.util.List;

import com.test.Vo.ReplyVo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReplyMapper {

   @Select("select * from reply where bno = #{bno}")
   public List<ReplyVo> getReplyAll(@Param("bno") int bno);


}