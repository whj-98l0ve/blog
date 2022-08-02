package com.whj.blog.service;

import com.whj.blog.po.User;

public interface UserService {

    User checkUser(String username,String password);

}
