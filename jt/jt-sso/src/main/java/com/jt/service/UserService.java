package com.jt.service;

import com.jt.pojo.User;
import java.util.List;

public interface UserService {
    boolean checkUser(String param, Integer type);

    List<User> findAll();
}
