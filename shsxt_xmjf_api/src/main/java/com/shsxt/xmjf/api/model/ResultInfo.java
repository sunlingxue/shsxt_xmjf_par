package com.shsxt.xmjf.api.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class ResultInfo implements Serializable {

    private static final long serialVersionUID = 252130220754103544L;

    private Integer code=200;

    private String msg="success";

    private Object result;

}
