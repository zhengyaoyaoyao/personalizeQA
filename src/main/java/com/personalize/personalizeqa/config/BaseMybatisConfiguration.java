package com.personalize.personalizeqa.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.utils.id.SnowflakeIdGenerate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis 常用重用拦截器
 *
 */
public abstract class BaseMybatisConfiguration {

    /**
     * id生成 机器码， 单机配置1即可。 集群部署，每个实例自增1即可。
     *
     * @param machineCode
     * @return
     */
    @Bean("snowflakeIdGenerate")
    public IdGenerate getIdGenerate(@Value("${id-generator.machine-code:1}") Long machineCode) {
        return new SnowflakeIdGenerate(machineCode);
    }

}
