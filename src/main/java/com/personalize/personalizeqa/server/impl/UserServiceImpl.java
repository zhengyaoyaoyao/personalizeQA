package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.dto.Result;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.mapper.UserMapper;
import com.personalize.personalizeqa.server.IUserService;
import com.personalize.personalizeqa.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public Result login(LoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            Result.fail("手机号格式错误");
        }
        User user = query().eq("phone",phone).one();
        return Result.ok(user);
    }

}
