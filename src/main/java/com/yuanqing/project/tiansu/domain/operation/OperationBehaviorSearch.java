package com.yuanqing.project.tiansu.domain.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSearch
 * @Description 操作行为检索实体类
 * @Date 2021/1/29 10:14
 * @Version V1.0
 */
public class OperationBehaviorSearch {


    private Integer num;

    private Integer size;


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
