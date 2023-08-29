package com.personalize.personalizeqa.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName("qa_dataset")
public class DataSet extends En<String> {
    private static final long serialVersionUID = 1L;
    /**
     * 数据集名称
     */
    @TableField(value = "data_name")
    private String dataName;
    /**
     * 数据集的来源
     */
    @TableField(value = "data_url")
    private String dataUrl;
    /**
     * 数据集介绍
     */
    @TableField(value = "data_info")
    private String dataInfo;
    /**
     * 此数据集拥有的文件数
     */
    @TableField(value = "files_size")
    private Long filesSize;
    /**
     * 文件所属文件夹
     */
    @TableField(value = "folder")
    private String folder;
    /**
     * 文件所属的直系文件夹
     */
    @TableField(value = "rel_folder")
    private String relFolder;
    /**
     * 数据集创建的年月
     */
    @TableField(value = "create_month")
    private String createMonth;
    /**
     * 数据集创建所属的周
     */
    @TableField(value = "create_week")
    private String createWeek;
    /**
     * 数据集创建年月日
     */
    @TableField(value = "create_day")
    private String createDay;
    @Builder
    public DataSet(String id,LocalDateTime createTime,String createUser,LocalDateTime updateTime,
                   String updateUser,String dataName,String dataUrl,String dataInfo,Long filesSize,String folder,String relFolder,String createMonth,String createWeek,
                   String createDay){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.dataName = dataName;
        this.dataUrl = dataUrl;
        this.dataInfo = dataInfo;
        this.filesSize = filesSize;
        this.folder = folder;
        this.relFolder = relFolder;
        this.createMonth = createMonth;
        this.createWeek = createWeek;
        this.createDay = createDay;
    }
}
