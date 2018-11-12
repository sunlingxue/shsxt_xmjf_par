package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.User;

public interface IUserService {

    public User queryUserByUserId(Integer userId);


    /**
     * 用户注册
     * @param phone
     * @param code
     * @param passWord
     */
    public void saveUser(String phone,String code,String passWord);


    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    public UserModel userLogin(String phone, String password);


    /**
     *快速登录
     * @param phone
     * @param code
     * @return
     */
    public UserModel quickLogin(String phone, String code);

}
