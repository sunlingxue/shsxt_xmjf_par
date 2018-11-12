package com.shsxt.xmjf.api.query;

import lombok.Data;

import java.io.Serializable;


@Data
public class BasItemQuery implements Serializable {
    private static final long serialVersionUID = -2530515259509784485L;

    private Integer itemCycle; //项目期限 1:0~30天 2:30~90 3:90以上

    private Integer itemType; //项目类型

    private Integer isHistory; //1:历史项目 0:可投项目

    private Integer pageNum=1;

    private Integer pageSize=10;

}
