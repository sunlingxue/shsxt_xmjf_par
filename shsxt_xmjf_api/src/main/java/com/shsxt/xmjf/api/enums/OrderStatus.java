package com.shsxt.xmjf.api.enums;


/**
 * 订单状态枚举类
 */
public enum OrderStatus {

    PAY_FAILED(0),//支付成功
    PAY_SUCCESS(1), //支付失败
    PAY_CHECKING(2); //审核中

    private Integer type;

    OrderStatus(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
