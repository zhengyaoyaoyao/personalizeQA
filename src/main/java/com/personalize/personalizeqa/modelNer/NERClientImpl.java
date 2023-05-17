package com.personalize.personalizeqa.modelNer;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Component
public class NERClientImpl implements NERClient{
    @Override
    public String getNERPost(String text) {
        String url = "http://127.0.0.1:5000/qa";
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //创建请求体
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("text",text);
        //创建HttpEntity，将请求体和请求头组装起来
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(body,headers);
        //创建RestTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url,requestEntity, String.class);
        //获取响应数据
        String responseBody = response.getBody();
        System.out.println(responseBody);
        //处理数据
        return response.getBody();
    }
}
