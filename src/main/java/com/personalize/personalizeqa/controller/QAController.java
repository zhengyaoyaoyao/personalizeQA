package com.personalize.personalizeqa.controller;

import com.personalize.personalizeqa.server.NERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/personalize")
@CrossOrigin // 允许来自指定源的请求
public class QAController {
    @Autowired
    private NERService nerService;
    @PostMapping("/qa")
    public String qa(@RequestBody Map<String,String> map){
        String result = nerService.qa(map.get("text"));
        return result;
    }
}
