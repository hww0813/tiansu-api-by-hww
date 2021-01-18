package com.yuanqing.framework.web.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * Manager基础模版
 *
 * @param <T>  实体
 * @param <PK> 主键
 * @author xucan
 */

@Validated
public interface BaseService<T, PK extends Serializable> {

    /**
     * 保存或更新
     *
     * @param entity 实体
     */
    PK save(@Valid @NotNull(message = "保存或更新的实体不能为空") T entity);

    /**
     * 删除
     *
     * @param id 主键
     */
    void deleteById(@Valid @NotNull(message = "根据ID删除的ID不能为空") PK id);

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return 实体
     */
    T findById(@Valid @NotNull(message = "根据ID查询的ID不能为空") PK id);

    /**
     * 获取列表
     * @param filters  筛选条件
     * @return 列表
     */
    List<T> getList(JSONObject filters);



}
