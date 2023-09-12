package com.personalize.personalizeqa.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class InfoSourceFileVO {
    private String id;
    private String taskCode;
    private String fileName;
    private String url;
    private Boolean status;
    private String createUser;
    private LocalDateTime createTime;

}
