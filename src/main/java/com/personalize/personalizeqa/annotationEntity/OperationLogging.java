package com.personalize.personalizeqa.annotationEntity;


import com.personalize.personalizeqa.enumeration.OperationType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface OperationLogging {
    /**
     * 操作内容描述
     */
    String value() default "";
    @AliasFor("value")
    String description() default "";
    /**
     * 操作类型
     */
    OperationType type() default OperationType.OTHER;
}
