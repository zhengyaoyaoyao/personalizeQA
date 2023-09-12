package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.dto.LoginFormDTO;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.vo.UserInfoVO;
import com.personalize.personalizeqa.vo.UserLoginInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {
    R<String> login(LoginFormDTO loginFormDTO, HttpServletRequest request);

    UserLoginInfoVO profile();

    R<Page<UserInfoVO>> findAll(Integer page, Integer perPage, String keyword);

    boolean isNotExist(String username);

    Boolean insert(String username, String nickName, String phone, String password, String organization, String authority, Boolean status);

    User searchUserById(String id);

    Boolean updateUser(String id, String username, String nickName, String phone, String password, String organization, String authority, Boolean status);

    Boolean deleteById(String id);

    List<Map<String, List<String>>> organizations();
}
