package com.lydia.multipledatasources.service;
import com.lydia.multipledatasources.entity.User;
import java.util.List;
/**
 * @description:
 * @author: Lydia Lee
 * */
public interface UserService {
    void insertTwoDBWithTX(String name);
    void insertTwoDBWithTXRollback(String name);
    void insetTwoDBProgrammatically(String name);
    List<User> getAllFirstDBUsers();
    List<User> getAllSecondDBUsers();
}
