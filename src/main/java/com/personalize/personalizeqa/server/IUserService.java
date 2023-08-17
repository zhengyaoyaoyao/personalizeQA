package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.dto.Result;
import com.personalize.personalizeqa.entity.User;

public interface IUserService extends IService<User> {
    Result login(LoginFormDTO loginFormDTO);
}
