package com.example.dc.mapper;

import com.example.dc.pojo.DocumentType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

public interface DocumentTypeMapper extends Mapper<DocumentType> {
    @Select("select document_cid from document_type where dtype=#{type}")
    Integer findCidByType(String type);
}
