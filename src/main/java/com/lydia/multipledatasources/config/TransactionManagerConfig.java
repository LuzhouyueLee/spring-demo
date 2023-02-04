package com.lydia.multipledatasources.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/**
 * @description: 配置并为Spring Boot注入了JTA TransactionManager
 * 实现为Atomikos提供的atomikos UserTransactionManager
 * @author: Lydia Lee
 */

@Configuration
public class TransactionManagerConfig {

    @Bean
    @Primary
    // 创建 AtomikosTransactionManager 用于分布式事务管理
    public UserTransactionManager atomikosTransactionManager(DataSource dataSource) {
        UserTransactionManager transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(true);
        return transactionManager;
    }

    //配置Spring的JtaTransactionManager，底层委派给atomikos进行处理
    @Bean
    public JtaTransactionManager jtaTransactionManager (UserTransactionManager userTransactionManager) {
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
