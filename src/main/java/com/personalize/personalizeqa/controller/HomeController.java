package com.personalize.personalizeqa.controller;

import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.ITaskService;
import com.personalize.personalizeqa.vo.HomeTaskInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ITaskService taskService;
    @GetMapping("/taskInfo")
    public R<HomeTaskInfoVO> taskInfo(){
        HomeTaskInfoVO  homeTaskInfoVO =  taskService.taskCompleteInfo();
        return R.success(homeTaskInfoVO);
    }
}
