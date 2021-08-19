package com.example.dc.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Autowired
    private RestTemplate restTemplate;

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

        ResponseEntity<String> apiResponse = restTemplate.postForEntity
                (
                        url,
                        generatePostJson(params),
                        String.class
                );
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

    /**
     * get请求、请求参数为?拼接形式的
     * <p>
     * 最终请求的URI如下：
     * <p>
     * http://127.0.0.1:80/?name=zhangsan&sex=男
     *
     * @return
     */
    public String sendGet() {
        Map<String, String> uriMap = new HashMap<>(6);
        uriMap.put("name", "张耀烽");
        uriMap.put("sex", "男");

        ResponseEntity responseEntity = restTemplate.getForEntity
                (
                        generateRequestParameters("http", "127.0.0.1:80", uriMap),
                        String.class
                );
        return (String) responseEntity.getBody();
    }


}
