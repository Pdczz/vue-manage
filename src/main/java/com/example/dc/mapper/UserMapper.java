package com.example.dc.mapper;

import com.example.dc.common.mapper.BaseMapper;
import com.example.dc.pojo.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username=#{username}")
    User queryByuserName(String username);
    //查询user_role表，根据userid查询rid
    @Select("SELECT ur.rid\n" +
            "FROM admin_user_role ur\n" +
            "WHERE uid=#{uid}")
    List<Integer> queryRoleidByUserid(@Param("uid")Integer uid);
    //根据uid删除rid
    @Delete("delete from admin_user_role where uid=#{uid} ")
    void deleteRidByUid(@Param("uid")Integer uid);
}
