package com.lydia.multipledatasources.config;

import com.lydia.multipledatasources.mapper.first.FirstUserMapper;
import com.lydia.multipledatasources.mapper.second.SecondUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: Lydia Lee
 */
@Configuration
public class InitData {
    @Autowired
    FirstUserMapper firstUserMapper;
    @Autowired
    SecondUserMapper secondUserMapper;

    @PostConstruct
    public void init() {
        firstUserMapper.init();
        secondUserMapper.init();

    }
}
