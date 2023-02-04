package com.lydia.multipledatasources.service;

import com.lydia.multipledatasources.mapper.second.SecondUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import com.lydia.multipledatasources.entity.User;
import com.lydia.multipledatasources.mapper.first.FirstUserMapper;

import java.util.List;

/**
 * @description:
 * @author: Lydia Lee
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    FirstUserMapper firstUserMapper;
    @Autowired
    SecondUserMapper secondUserMapper;

    @Autowired
    JtaTransactionManager jtaTransactionManager;

    @Override
    //1.使用注释型@Transactional
    //2.成功向DB1和DB2插入数据，并提交成功
    @Transactional(rollbackFor = Throwable.class)
    public void insertTwoDBWithTX(String name) {
        User user = new User();
        user.setName(name);
        // 会回滚
        firstUserMapper.insert(user);
        // 不会回滚
        secondUserMapper.insert(user);
    }

    //1.使用注释型@Transactional
    //2.事物开始后，由于除以0的异常，全局事务触发rollback（DB1和DB2都触发rollback）
    //虽然DB1和DB2执行了insert，但是由于两阶段提交模型，在事务结束前抛出了异常，因此DB1和DB2数据并没有更改
    @Transactional(rollbackFor = Throwable.class)
    public void insertTwoDBWithTXRollback(String name) {
        User user = new User();
        user.setName(name);
        firstUserMapper.insert(user);
        secondUserMapper.insert(user);

        // 主动触发回滚
        int i = 1/0;
    }

    //编程式的分布式事务，使用了JTATransactionManager和TransactionTemplate，发生除以0的异常，触发rollback
    //因此DB1和DB2数据并没有插入用户
    @Override
    public void insetTwoDBProgrammatically(String name) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(jtaTransactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                User user = new User();
                user.setName(name);
                firstUserMapper.insert(user);
                secondUserMapper.insert(user);
                // 主动触发回滚
                int i = 1/0;
            }
        });

    }

    @Override
    public List<User> getAllFirstDBUsers() {
        return firstUserMapper.selectAll();
    }

    @Override
    public List<User> getAllSecondDBUsers() {
        return secondUserMapper.selectAll();
    }

}
