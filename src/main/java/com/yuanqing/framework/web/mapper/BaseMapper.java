package com.yuanqing.framework.web.mapper;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xucan on 2021-01-15 16:32
 * @author xucan
 */

public interface BaseMapper<T, PK extends Serializable> {

    /**
     * 插入一条记录
     *
     * @param entity 实体
     * @return
     */
    void insert(T entity);

    /**
     * 更新一条记录
     *
     * @param entity 实体
     */
    void update(T entity);

    /**
     * 查找实体
     *
     * @param id 主键
     * @return 实体
     */
    T findById(PK id);

    /**
     * 删除
     *
     * @param id 主键
     */
    void delete(PK id);

    /**
     * 获取列表
     *
     * @param filters 筛选条件
     * @return 符合条件的记录
     */
    List<T> getList(JSONObject filters);

}
