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
@TableName("qa_file")
public class File extends En<String> {
    @TableField(value = "data_id")
    private String dataId;
    /**
     * 数据类型
     */
    @TableField("data_type")
    private DataType dataType;

    /**
     * 原文件名
     */
    @TableField("submitted_file_name")
    private String submittedFileName;
    /**
     * 文件是否被删除
     */
    @TableField("is_delete")
    private Boolean isDelete;

    /**
     * 记录所属的文件夹
     */
    @TableField("folder")
    private String folder;
    /**
     * 文件下载的url
     */
    @TableField("url")
    private String url;
    /**
     * 文件相对于服务器的路径
     */
    @TableField("relative_path")
    private String relativePath;
    /**
     * 服务器上这个文件叫什么名字
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 文件后缀
     */
    @TableField("ext")
    private String ext;

    /**
     * 文件大小
     */
    @TableField("size")
    private Long size;
    /**
     * 文件类型
     */
    @TableField("context_type")
    private String contextType;
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
    public File(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime, String updateUser,
                String dataId,DataType dataType,String submittedFileName,Boolean isDelete,String folder,String url,
                String relativePath,String fileName,String ext,Long size,String contextType,
                String group,String path){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.dataId = dataId;
        this.dataType = dataType;
        this.submittedFileName = submittedFileName;
        this.isDelete = isDelete;
        this.folder = folder;
        this.url = url;
        this.relativePath = relativePath;
        this.fileName = fileName;
        this.ext = ext;
        this.size = size;
        this.contextType = contextType;
        this.group = group;
        this.path = path;
    }
}
