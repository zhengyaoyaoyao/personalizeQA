package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.IDataCollectionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/data-collection")
@Api(value = "信源数据收集",tags = "信源数据收集")
public class dataCollectionController {
    @Autowired
    private IDataCollectionService dataCollectionService;
    @GetMapping("/popsci")
    public R<String> PopsciSource(@RequestParam(value="id",required = false) String id,@RequestParam(value="api",required = false) String api,@RequestParam(value="is_connect",required = true) boolean isConnect,@RequestParam(value="mongodbSet",required = false) String mongodbSet){
        if (isConnect){
            return dataCollectionService.pingSource(api);
        }else {
            return dataCollectionService.callSource(id,api,mongodbSet);
        }
    }
}
