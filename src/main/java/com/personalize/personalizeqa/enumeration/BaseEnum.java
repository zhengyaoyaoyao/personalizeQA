package com.personalize.personalizeqa.enumeration;

import java.util.Arrays;
import java.util.Map;

/**
 * 枚举类型基类
 * 在jdk8以后，java的接口是可以有具体的实现方法的，但是需要有static和default修饰
 * static：接口对象不需要创建就可以直接调用
 * default：如果不进行实现，那么就默认有这个方法，相当于extents
 */
public interface BaseEnum {
    /**
     * 将制定的枚举集合转成 map
     * key -> name
     * value -> desc
     *
     * @param list
     * @return
     */
    static Map<String, String> getMap(BaseEnum[] list) {
        return MapHelper.uniqueIndex(Arrays.asList(list), BaseEnum::getCode, BaseEnum::getDesc);
    }
    /**
     * 编码重写
     * @return
     */
    default String getCode() {
        return toString();
    }
    /**
     * 描述
     * @return
     */
    String getDesc();
}
