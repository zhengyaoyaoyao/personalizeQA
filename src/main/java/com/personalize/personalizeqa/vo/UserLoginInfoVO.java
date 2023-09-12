package com.personalize.personalizeqa.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserLoginInfoVO {
    private String id;
    private String nickName;
    private String username;
    private List<String> authority;
}
