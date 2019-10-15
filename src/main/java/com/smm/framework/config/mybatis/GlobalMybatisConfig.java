package com.smm.framework.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alan Chen
 * @description Mybatis
 * @date 2019/5/15
 */
@Configuration
public class GlobalMybatisConfig {
    /**
     * mybatis分页
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 注入公共字段（除了注入公共字段还可以使用自动填充）
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new GlobalMetaObjectHandler();
    }
}
