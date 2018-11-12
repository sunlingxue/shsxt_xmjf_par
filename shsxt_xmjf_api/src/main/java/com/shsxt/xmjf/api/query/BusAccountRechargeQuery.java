package com.shsxt.xmjf.api.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusAccountRechargeQuery implements Serializable {
    private static final long serialVersionUID = -4803640833929384171L;
    private Integer userId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;

}
