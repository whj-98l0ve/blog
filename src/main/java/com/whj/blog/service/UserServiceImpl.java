package com.whj.blog.service;

import com.whj.blog.dao.UserRepository;
import com.whj.blog.po.User;
import com.whj.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));//密码是123456 好像是 还是7个1来着？谷歌有存储




        return user;
    }
}
