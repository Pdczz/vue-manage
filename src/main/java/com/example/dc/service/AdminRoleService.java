package com.example.dc.service;

import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.mapper.*;
import com.example.dc.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminMenuService adminMenuService;
    @Autowired
    private AdminPermissionService adminPermissionService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;


    @Transactional        //根据rid修改Menu
    public void updateRoleMenu(int rid, Map menusIds) {
        roleMapper.deletemidByRid(rid);
        List<AdminRoleMenu> rms = new ArrayList<>();
        if (!CollectionUtils.isEmpty(menusIds)){
           for (Integer mid : (List<Integer>)menusIds.get("menusIds")) {
               AdminRoleMenu rm = new AdminRoleMenu();
               rm.setMid(mid);
               rm.setRid(rid);
               rms.add(rm);
            }
        }
        if (!CollectionUtils.isEmpty(rms)){
            roleMenuMapper.insertList(rms);
        }
    }
    @Transactional       //根据rid修改permission
    public void savePermChanges(int rid, List<AdminPermission> perms) {
        AdminRolePermission rp=new AdminRolePermission();
        rp.setRid(rid);
        rolePermissionMapper.delete(rp);
        List<AdminRolePermission> rps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(perms)){
            perms.forEach(p -> {
                AdminRolePermission rp2=new AdminRolePermission();
                rp2.setRid(rid);
                rp2.setPid(p.getId());
                rps.add(rp2);
            });
        }
        if (!CollectionUtils.isEmpty(rps)){
            rolePermissionMapper.insertList(rps);
        }
    }

    /**
     * 更新role，并修改中间表
     * @param role
     */
    public void editRole(AdminRole role) {
        roleMapper.updateByPrimaryKeySelective(role);
        savePermChanges(role.getId(), role.getPerms());
    }

    @Transactional
    public void saveRoleChanges(int uid, List<AdminRole> roles) {
        userMapper.deleteRidByUid(uid);
        List<AdminUserRole> urs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)){
            roles.forEach(r -> {
                AdminUserRole ur = new AdminUserRole();
                ur.setUid(uid);
                ur.setRid(r.getId());
                urs.add(ur);
            });
        }
        if (!CollectionUtils.isEmpty(urs)){
            userRoleMapper.insertList(urs);
        }
    }


    /**
     * 根据uid查询Role
     * @param uid
     * @return
     */
    public List<AdminRole> listRoleByUser(Integer uid) {
        List<Integer> roleId = roleMapper.queryRoleByUid(uid);
        if (!CollectionUtils.isEmpty(roleId)){
            List<AdminRole> adminRoles = roleMapper.selectByIdList(roleId);
            return adminRoles;
        }
        return null;
    }
    /**
     * 为Role设置权限和菜单
     * @return
     */
    public List<AdminRole> listWithPermsAndMenus() {
        List<AdminRole> roles = roleMapper.selectAll();
        List<AdminPermission> perms;
        List<AdminMenu> menus;
        if (!CollectionUtils.isEmpty(roles)){
            for (AdminRole role : roles) {
                perms = adminPermissionService.listPermsByRoleId(role.getId());
                menus = adminMenuService.getMenusByRoleId(role.getId());
                role.setPerms(perms);
                role.setMenus(menus);
            }
        }
        return roles;
    }

    public void add(AdminRole requestRole) {
        int insert = roleMapper.insert(requestRole);
        if (insert!=1){
            throw new LyException(ExceptionEnum.ROLE_SAVE_ERROR);
        }
    }
}
