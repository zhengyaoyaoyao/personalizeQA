package com.personalize.personalizeqa.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private String id;
    private String username;
    private String userCode;
    private String nickName;
    private String authority;
    private String organization;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime loginTime;
    private String loginIp;
    private Boolean status;
}
