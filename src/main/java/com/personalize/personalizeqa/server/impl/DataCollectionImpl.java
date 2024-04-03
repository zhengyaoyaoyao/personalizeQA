package com.personalize.personalizeqa.server.impl;

import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Task;
import com.personalize.personalizeqa.mapper.TaskMapper;
import com.personalize.personalizeqa.server.IDataCollectionService;
import com.personalize.personalizeqa.server.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
@Service
public class DataCollectionImpl implements IDataCollectionService {
    @Value("${python.dataCollection.uri-prefix}")
    private String pythonPrefixUrl;
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public R<String> pingSource(String api) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> params = new HashMap<>();
        String url = pythonPrefixUrl+api;
        params.put("is_connect","true");
        try{
            String response = restTemplate.postForObject(url, params,String.class);
            return R.success("Api is up and running","Api is up and running");
        }catch (Exception e){
            return R.fail("Api is close");
        }
    }

    @Override
    public R<String> callSource(String id,String api,String mongodbSet) {
        Task task = taskMapper.selectById(id);
        String url = pythonPrefixUrl+api;
        String startTime = transDateFormat(task.getStartTime());
        String endTime = transDateFormat(task.getEndTime());
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("mongodbSet",mongodbSet);
        params.put("start_time", startTime);
        if (endTime != null) {
            params.put("end_time", endTime);
        }
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, params, String.class);
        return R.success(response);
    }
    public String transDateFormat(Long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(instant.atZone(ZoneId.systemDefault()));
        return format;
    }
}
