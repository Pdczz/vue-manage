package com.example.dc.mapper;

import com.example.dc.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryMapper extends Mapper<com.example.dc.pojo.Category> {
    @Select("select * from category where id=#{id}")
    Category queryCategoryById(@Param("id")Integer id);
}
