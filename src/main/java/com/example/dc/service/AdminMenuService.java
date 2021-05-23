package com.example.dc.service;

import com.example.dc.mapper.MenuMapper;
import com.example.dc.mapper.RoleMapper;
import com.example.dc.mapper.UserMapper;
import com.example.dc.pojo.AdminMenu;
import com.example.dc.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminMenuService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = roleMapper.queryMidsByRid(rid);
        if (!CollectionUtils.isEmpty(menuIds)){
            List<AdminMenu> menus = menuMapper.selectByIdList(menuIds);
            handleMenus(menus);
            return menus;
        }
        return null;

    }
    public List<AdminMenu> getMenusByCurrentUser() {
        // 从数据库中获取当前用户
        String username="";
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal!=null){
            username=principal.toString();
        }
        User user = userMapper.queryByuserName(username);
        List<AdminMenu> menus=null;
        // 获得当前用户对应的所有角色的 id 列表
        if (user!=null){
            List<Integer> rids = userMapper.queryRoleidByUserid(user.getId());

            List<Integer> menuIds=null;
            // 查询出这些角色对应的所有菜单项
            if (!CollectionUtils.isEmpty(rids)){
                menuIds = roleMapper.queryMidsByRids(rids)
                        .stream().distinct().collect(Collectors.toList());
            }

            if (!CollectionUtils.isEmpty(menuIds)){
                menus = menuMapper.selectByIdList(menuIds);
            }
            if (!CollectionUtils.isEmpty(menus)){
                handleMenus(menus);
            }
        }

        // 处理菜单项的结构

        return menus;
    }

    public void handleMenus(List<AdminMenu> menus) {
        menus.forEach(m -> {
            List<AdminMenu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });
        menus.removeIf(m -> m.getParentId() != 0);
    }

    private List<AdminMenu> getAllByParentId(int id) {
        AdminMenu am=new AdminMenu();
        am.setParentId(id);
        return menuMapper.select(am);
    }

}
