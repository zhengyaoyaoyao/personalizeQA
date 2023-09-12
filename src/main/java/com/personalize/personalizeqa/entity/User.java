package com.personalize.personalizeqa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("qa_user")
public class User extends En<String> {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @TableField("username")
    private String username;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;
    /**
     * 所属组织
     */
    @TableField("organization")
    private String organization;
    /**
     * 权限信息
     */
    @TableField("authority")
    private String authority;
    /**
     * 最后登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;
    /**
     * 最后登录IP
     */
    @TableField("login_ip")
    private String loginIP;
    @TableField("status")
    private boolean status;
    @Builder
    public User(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime,String updateUser,
                String username,String phone,String password,String nickName,String organization,String authority,
                LocalDateTime loginTime,String loginIP,Boolean status){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.nickName = nickName;
        this.organization = organization;
        this.authority = authority;
        this.loginTime = loginTime;
        this.loginIP = loginIP;
        this.status = status;
    }
}
