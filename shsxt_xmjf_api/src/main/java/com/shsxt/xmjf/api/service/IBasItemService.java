package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.Utils.PageList;
import com.shsxt.xmjf.api.query.BasItemQuery;

public interface IBasItemService {
    public PageList queryBasItemByParams(BasItemQuery basItemQuery);

    public void updateBasItemStatusToOpen(Integer itemId);
}
