package com.lydia.multipledatasources.mapper;

import com.lydia.multipledatasources.entity.User;
import com.lydia.multipledatasources.mapper.first.FirstUserMapper;
import com.lydia.multipledatasources.mapper.second.SecondUserMapper;
import com.lydia.multipledatasources.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    FirstUserMapper firstUserMapper;
    @Autowired
    SecondUserMapper secondUserMapper;

    @Autowired
    UserService userService;

    @Test
    public void testMapper() {
        User user1 = new User();
        user1.setName("He he");

        firstUserMapper.insert(user1);
        List<User> firstUsers = firstUserMapper.selectAll();

        System.out.println(firstUsers);

        User user2 = new User();
        user2.setName("Ha ha");

        secondUserMapper.insert(user2);
        List<User> secondUsers = secondUserMapper.selectAll();

        System.out.println(secondUsers);
    }

    @Test
    public void testUserService() {
        userService.insertTwoDBWithTX("Lydia");
        userService.insertTwoDBWithTX("Wish");
        List<User> firstUsers = firstUserMapper.selectAll();
        System.out.println("firstDB" + firstUsers);
        List<User> secondUsers = secondUserMapper.selectAll();
        System.out.println("secondDB" + secondUsers);
    }

    @Test
    public void testUserServiceWithRollback() {
        try {
            //成功插入TinyPiggy
            userService.insertTwoDBWithTX("TinyPiggy");
            //事务回滚，没有插入Hululu
            userService.insetTwoDBProgrammatically("Hululu");
        } catch(Exception t) {
//            t.printStackTrace();
        } finally {
            List<User> firstUsers = firstUserMapper.selectAll();
            System.out.println("firstDB-" + firstUsers);
            List<User> secondUsers = secondUserMapper.selectAll();
            System.out.println("secondDB-" + secondUsers);
        }

    }

}
