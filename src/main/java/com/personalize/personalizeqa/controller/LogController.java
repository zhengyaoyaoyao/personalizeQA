package com.personalize.personalizeqa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.entity.Log;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/log")
public class LogController {
    @Autowired
    private ILogService logService;
    @GetMapping("/findall")
    public R<Page<Log>> getAllLog(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        R<Page<Log>> logList = logService.findAll(page,perPage,keyword);
        return logList;
    }
}
