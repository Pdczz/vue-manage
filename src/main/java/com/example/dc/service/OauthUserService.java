package com.example.dc.service;

import com.example.dc.mapper.OauthUserMapper;
import com.example.dc.pojo.OauthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthUserService {
    @Autowired
    private OauthUserMapper oauthUserMapper;

    public OauthUser findOauthUserById(Integer id){
        OauthUser oauthUser = oauthUserMapper.selectByPrimaryKey(id);
        return oauthUser;
    }
    public OauthUser findOauthUserByAppId(String appId){
        if (StringUtils.isEmpty(appId)){
            return null;
        }
        OauthUser oauthUser = new OauthUser();
        oauthUser.setAppId(appId);
        return  oauthUserMapper.selectOne(oauthUser);
    }
}
