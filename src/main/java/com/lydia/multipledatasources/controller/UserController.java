package com.lydia.multipledatasources.controller;

import com.lydia.multipledatasources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lydia.multipledatasources.entity.User;

import java.util.List;

/**
 * @description:
 * @author: Lydia Lee
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("allUsersFromFirst")
    public List<User> allUsersFromFirst() {
        return userService.getAllFirstDBUsers();

    }
    @GetMapping("allUsersFromSecond")
    public List<User> allUsersFromSecond() {
        return userService.getAllSecondDBUsers();
    }
    @PostMapping("insertTwoDB")
    public void insertTwoDB(@RequestParam String name){

        userService.insertTwoDBWithTX(name);
    }

}
