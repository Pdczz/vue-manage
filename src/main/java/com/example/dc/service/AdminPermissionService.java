package com.example.dc.service;

import com.example.dc.mapper.PermissionMapper;
import com.example.dc.mapper.RoleMapper;
import com.example.dc.mapper.UserMapper;
import com.example.dc.pojo.AdminPermission;
import com.example.dc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPermissionService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public List<AdminPermission> queryAll(){
        return permissionMapper.selectAll();
    }
    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = permissionMapper.selectAll();
        for (AdminPermission p: ps) {
            // match prefix
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }
        return false;
    }
    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<Integer> pids =roleMapper.queryPidsByRid(rid);
        if (!CollectionUtils.isEmpty(pids)){
            return permissionMapper.selectByIdList(pids);
        }
        return null;
    }
    public Set<String> listPermissionURLsByUser(String username) {
        User user = userMapper.queryByuserName(username);
        // 获得当前用户对应的所有角色的 id 列表
        List<Integer> rids = userMapper.queryRoleidByUserid(user.getId());
        //根据角色ID列表查询PermissionId
        List<Integer> pids = roleMapper.queryPidsByRids(rids);
        //查询所有Permission
        if (CollectionUtils.isEmpty(pids)){
            return null;
        }
        List<AdminPermission> perms = permissionMapper.selectByIdList(pids);

        Set<String> URLs = perms.stream().map(AdminPermission::getUrl)
                .collect(Collectors.toSet());

        return URLs;
    }

}
