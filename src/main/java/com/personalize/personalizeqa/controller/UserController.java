package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.dto.Result;
import com.personalize.personalizeqa.server.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO){
        // TODO 实现登录功能
        return userService.login(loginFormDTO);
    }
}
