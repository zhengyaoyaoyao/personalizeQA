package com.personalize.personalizeqa.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型
 */
@Getter
@AllArgsConstructor
@JsonFormat
public enum DataType implements BaseEnum{
    /**
     * DIR="目录"
     */
    DIR("目录"),

    /**
     * IMAGE="图片"
     * @return
     */
    IMAGE("图片"),
    /**
     * 视频
     */
    VIDEO("视频"),
    /**
     * 音频
     */
    AUDIO("音频"),
    DOC("文档"),
    OTHER("其他")
    ;

    private String desc;//成员属性是给这个枚举成员增加属性的。
    public static DataType match(String val,DataType def){
        for (DataType enm :DataType.values()){
            if (enm.name().equalsIgnoreCase(val)){
                return enm;
            }
        }
        return def;
    }

    /*
    得到一个类型，如果没有这个类型，那么就是空
     */
    public static DataType get(String val){
        return match(val,null);
    }

    /**
     * 判断是哪个类型
     */
    public boolean eq(String val){
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(DataType val){
        if (val==null){
            return false;
        }
        return eq(val.name());
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
