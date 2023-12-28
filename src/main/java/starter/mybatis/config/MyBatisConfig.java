package starter.mybatis.config;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import starter.mybatis.typehandler.UUIDTypeHandler;

import java.util.List;
import java.util.UUID;

/**
 * Created by Yoo Ju Jin(jujin@100fac.com)
 * Created Date : 12/28/23
 * Copyright (C) 2023, Centum Factorial all rights reserved.
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(UUID.class, JdbcType.VARCHAR, new UUIDTypeHandler());
        };
    }
}
