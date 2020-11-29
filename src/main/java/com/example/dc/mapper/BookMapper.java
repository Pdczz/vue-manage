package com.example.dc.mapper;

import com.example.dc.pojo.Book;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface BookMapper extends Mapper<Book> {
    @Select("select * from book where cid=#{cid}")
    List<Book> queryByCategory(@Param("cid") Integer cid);

    Book queryByxml(Integer id);
}
