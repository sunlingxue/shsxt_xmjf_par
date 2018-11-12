package com.shsxt.xmjf.api.service;


/**
 * 短信发送
 */
public interface ISmsService {

    public void sendSms(String phone,Integer type);
}
