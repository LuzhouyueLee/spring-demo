package com.lydia.multipledatasources.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @description:
 * @author: Lydia Lee
 */
@Configuration
//配置 mapper 的扫描位置，指定相应的 sqlSessionTemplate
@MapperScan(basePackages = "com.lydia.multipledatasources.mapper.first", sqlSessionTemplateRef = "firstSqlSessionTemplate")
public class FirstDataSourceConfig {
    @Autowired
    FirstDataSourceHelper dataSourceHelper;

    @Bean
    @Primary
    // 读取配置，创建Atomiko数据源
    public DataSource firstDataSource() {
        // 设置数据库连接
        org.h2.jdbcx.JdbcDataSource h2XADataSource = new org.h2.jdbcx.JdbcDataSource();
        h2XADataSource.setUser(dataSourceHelper.getUsername());
        h2XADataSource.setPassword(dataSourceHelper.getPassword());
        h2XADataSource.setURL(dataSourceHelper.getJdbcUrl());

        // 事务管理器
        AtomikosDataSourceBean firstDataSource = new AtomikosDataSourceBean();
        firstDataSource.setXaDataSource(h2XADataSource);
        firstDataSource.setUniqueResourceName("firstDataSource");
        return firstDataSource;
    }



    @Bean
    @Primary
    // 创建 SqlSessionFactory
    public SqlSessionFactory firstSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置 xml 的扫描路径
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/first/*.xml"));
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(config);
        return bean.getObject();
    }

    @Bean
    @Primary
    // 创建 SqlSessionTemplate
    public SqlSessionTemplate firstSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
