package com.shsxt.xmjf.server.db.dao;


import com.shsxt.xmjf.api.po.User;

public interface UserDao {

    public User queryUserByUserId(Integer userId);
}
