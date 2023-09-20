package com.personalize.personalizeqa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.personalize.personalizeqa.enumeration.DataType;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qa_infosource_files")
public class InfoSourceFile extends En<String>{
    @TableField("task_id")
    private String taskId;
    @TableField("task_code")
    private String taskCode;
    @TableField("data_type")
    private DataType dataType;
    @TableField("context_type")
    private String contextType;
    @TableField("submitted_file_name")
    private String submittedFileName;
    @TableField("is_delete")
    private boolean isDelete;
    @TableField("url")
    private String url;
    @TableField("relative_path")
    private String relativePath;
    @TableField("file_name")
    private String fileName;
    @TableField("ext")
    private String ext;
    @TableField("size")
    private Long size;
    @TableField("status")
    private Boolean status;
    /**
     * FastDFS组
     * 用于FastDFS
     */
    @TableField("group_")
    private String group;

    /**
     * FastDFS远程文件名
     * 用于FastDFS
     */
    @TableField("path")
    private String path;
    @Builder
    public InfoSourceFile(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime, String updateUser,
                          String taskId,String taskCode,DataType dataType, String contextType,String submittedFileName,
                          Boolean isDelete,String url,String relativePath,String fileName,String ext,Long size,Boolean status,
                          String group,String path){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.taskId = taskId;
        this.taskCode = taskCode;
        this.dataType = dataType;
        this.contextType = contextType;
        this.submittedFileName=  submittedFileName;
        this.isDelete = isDelete;
        this.url = url;
        this.relativePath = relativePath;
        this.fileName = fileName;
        this.ext = ext;
        this.size = size;
        this.status = status;
        this.group = group;
        this.path = path;
    }


}
