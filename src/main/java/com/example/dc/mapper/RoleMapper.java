package com.example.dc.mapper;

import com.example.dc.common.mapper.BaseMapper;
import com.example.dc.pojo.AdminRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<AdminRole> {
    //根据rids查询中间表查询mids
    @Select({
            "<script>",
            "select",
            "mid",
            "from admin_role_menu",
            "where rid in",
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>",
            "#{rid}",
            "</foreach>",
            "</script>"
    })
    List<Integer> queryMidsByRids(@Param("rids")List<Integer> rids);

    @Select({
            "<script>",
            "select",
            "pid",
            "from admin_role_permission",
            "where rid in",
            "<foreach collection='rids' item='rid' open='(' separator=',' close=')'>",
            "#{rid}",
            "</foreach>",
            "</script>"
    })
    List<Integer> queryPidsByRids(@Param("rids")List<Integer>rids);
    @Select("select pid from admin_role_permission where rid=#{rid}")
    List<Integer> queryPidsByRid(@Param("rid")Integer rid);
    @Select("select mid from admin_role_menu where rid=#{rid}")
    List<Integer> queryMidsByRid(@Param("rid")Integer rid);

    @Select("select rid from admin_user_role where uid=#{uid}")
    List<Integer> queryRoleByUid(@Param("uid")Integer uid);

    //根据rid删除menuId
    @Delete("delete from admin_role_menu where rid=#{rid} ")
    void deletemidByRid(@Param("rid")Integer rid);
}
