package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xucan on 2021-01-19 10:58
 * @author xucan
 */
@Repository
public interface ServerTreeMapper extends BaseMapper<ServerTree,Long> {
    //根据id查询数据
    ServerTree findById(Long id);

    List<ServerTree> findByServerCode(String serverCode);

    List<ServerTree> findOne(@Param("serverIp") Long serverIp/*, @Param("serverMac") String serverMac,@Param("serverPort") Integer serverPort*/);

    List<ServerTree> getSessionServerList(JSONObject jsonObject);

    /*批量新增*/
    void batchInsert(List<ServerTree> list);

    /*无条件的获得全部数据*/
    List<ServerTree> getAllDate();

    //获取一个id
    Long findId ();

    public void insertServerTree(ServerTree serverTree);
}
