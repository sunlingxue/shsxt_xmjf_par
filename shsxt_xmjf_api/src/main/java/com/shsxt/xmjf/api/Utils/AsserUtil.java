package com.shsxt.xmjf.api.Utils;

import com.shsxt.xmjf.api.exceptions.ParamsException;

public class AsserUtil {
    public static void isTrue(Boolean flag ,String msg){
        if (flag){
            throw new ParamsException(msg);
        }
    }

}
