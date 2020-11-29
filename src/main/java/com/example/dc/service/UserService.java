package com.example.dc.service;

import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.mapper.UserMapper;
import com.example.dc.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminRoleService adminRoleService;

    public User getLoad(User user){
        return userMapper.selectOne(user);
    }
    public List<User> queryAll(){
        return userMapper.selectAll();
    }

    public User queryByUsername(String username){
        return userMapper.queryByuserName(username);
    }


    public boolean isExist(String username) {
        User user = userMapper.queryByuserName(username);
        if (user!=null){
            return true;
        }
        return false;
    }

    public void add(User user) {
        int insert = userMapper.insert(user);
        if (insert!=1){
            throw new LyException(ExceptionEnum.USER_SAVE_ERROR);
        }
    }

    // UserService
    public void editUser(User user) {
        User userInDB = userMapper.queryByuserName(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userMapper.updateByPrimaryKeySelective(userInDB);

        adminRoleService.saveRoleChanges(userInDB.getId(), user.getRoles());
    }

    public void updateUserStatus(User user) {
        User userInDB = userMapper.selectOne(user);
        userInDB.setEnabled(user.isEnabled());
        userMapper.updateByPrimaryKeySelective(userInDB);
    }

    public void resetPassword(User user) {
        User selectOne = userMapper.selectOne(user);
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        selectOne.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        selectOne.setPassword(encodedPassword);
        int i = userMapper.updateByPrimaryKeySelective(selectOne);
        if (i!=1){
            throw new LyException(ExceptionEnum.BOOKS_UPDATE_ERROR);
        }
    }
}
