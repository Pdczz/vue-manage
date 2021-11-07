package com.example.dc.service;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dc.common.enums.ExceptionEnum;
import com.example.dc.common.exception.LyException;
import com.example.dc.common.utils.UserInfo;
import com.example.dc.mapper.OauthUserMapper;
import com.example.dc.pojo.GithubUser;
import com.example.dc.pojo.OauthUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OauthUserMapper oauthUserMapper;
    @Autowired
    private OauthUserService oauthUserService;

    public GithubUser getToken(String code){
        GithubUser githubUser=new GithubUser();
        if (StringUtils.isNotEmpty(code)){
            //获取到授权码,获取token
            String url="https://github.com/login/oauth/access_token";
            HashMap<String, String> jsonParams = new HashMap<>();
            jsonParams.put("client_id","fe0755eb905bb2cadfb5");
            jsonParams.put("client_secret","3d5c32de1b3c16422ea6292f027bad7469833c16");
            jsonParams.put("code",code);
            String s="";
            try {
                s = sendPost(url, jsonParams);
            }catch (Exception exception){
                //github响应异常
                throw new LyException(ExceptionEnum.GITHUB_LOGIN_ERROR);
            }
            System.out.println(s);
            String[] split = s.split("&");
            String[] token = split[0].split("=");
            String access_token = token[1];
            String githubURL="https://api.github.com/user";
            if (StringUtils.isNotEmpty(access_token)){
                HttpHeaders headers = new HttpHeaders();
                headers.set("authorization", "Bearer "+access_token);
                //过滤掉账号认证失败的时候抛出的401异常
                restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        if(response.getRawStatusCode() == 401){
                            //code过期
                            throw new LyException(ExceptionEnum.GITHUB_CODE_EXPIRED);
                        }
                    }
                });
                ResponseEntity<Object> response = restTemplate.exchange(githubURL, HttpMethod.GET,new HttpEntity<byte[]>(headers),Object.class);
                Object body = response.getBody();
                JSON json = (JSON) JSONObject.toJSON(body);
                System.out.println(json);
                githubUser = JSON.toJavaObject(json, GithubUser.class);
                OauthUser oauthUser = new OauthUser();
                String appId = githubUser.getId();
                OauthUser oauthUserByAppId = oauthUserService.findOauthUserByAppId(appId);
                if (oauthUserByAppId==null){
                    oauthUser.setAppId(appId);
                    oauthUser.setAvatar(githubUser.getAvatar_url());
                    oauthUser.setEmail(githubUser.getEmail());
                    oauthUser.setUsername(githubUser.getName());
                    oauthUser.setCreateTime(new Date());
                    int insert = oauthUserMapper.insert(oauthUser);
                    System.out.println(insert);
                }else {
                    oauthUserByAppId.setAvatar(githubUser.getAvatar_url());
                    oauthUserByAppId.setEmail(githubUser.getEmail());
                    oauthUserByAppId.setUsername(githubUser.getName());
                    oauthUserMapper.updateByPrimaryKeySelective(oauthUserByAppId);
                }
                UserInfo userInfo = new UserInfo();


            }
        }
        return githubUser;
    }



    public HttpEntity<Map<String, String>> generatePostJson(Map<String, String> jsonMap) {

        //如果需要其它的请求头信息、都可以在这里追加
        HttpHeaders httpHeaders = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");

        httpHeaders.setContentType(type);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(jsonMap, httpHeaders);

        return httpEntity;
    }


    /**
     * post请求、请求参数为json
     *
     * @return
     */
    public String sendPost(String url,Map params) {
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }
        });
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, generatePostJson(params), String.class);
        return apiResponse.getBody();
    }
    public String generateRequestParameters(String protocol, String uri, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(protocol).append("://").append(uri);
        if (!CollectionUtils.isEmpty(params)) {
            sb.append("?");
            for (Map.Entry map : params.entrySet()) {
                sb.append(map.getKey())
                        .append("=")
                        .append(map.getValue())
                        .append("&");
            }
            uri = sb.substring(0, sb.length() - 1);
            return uri;
        }
        return sb.toString();
    }




}
