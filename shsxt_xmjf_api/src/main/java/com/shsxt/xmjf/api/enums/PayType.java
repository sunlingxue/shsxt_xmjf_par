package com.shsxt.xmjf.api.enums;

public enum PayType {

    APP(1),

    ADMIN(2),

    PC(3),

    WEI_XIN(4);

    private Integer type;

    PayType(Integer type) {
        this.type = type;
    }

    public Integer getType() {

        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
