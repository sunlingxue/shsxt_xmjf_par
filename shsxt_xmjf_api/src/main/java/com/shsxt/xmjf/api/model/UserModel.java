package com.shsxt.xmjf.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserModel implements Serializable {

    private static final long serialVersionUID = -8304673605011919760L;
    private Integer id;
    private String userName;
    private String phone;
}
