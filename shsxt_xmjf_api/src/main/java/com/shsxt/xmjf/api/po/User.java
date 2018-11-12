package com.shsxt.xmjf.api.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 7158917681013094565L;
    private Integer id;
    private String userName;
    private String userPwd;

}
