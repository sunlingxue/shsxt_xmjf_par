package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUser;

public interface BaseMapper<T> {
    public  Integer save(T entity);
    public T queryById(Integer id);
    public Integer update(T entity);
    public  Integer delete(Integer id);
}
