package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.query.BasItemQuery;

import java.util.List;
import java.util.Map;

public interface BasItemMapper extends BaseMapper<BasItem> {

    /**
     * 多条件查询
     * @param basItemQuery
     * @return
     */
    public List<Map<String,Object>> queryBasItemsByParams(BasItemQuery basItemQuery);

    public void updateBasItemStatusToOpen(Integer itemId);
}