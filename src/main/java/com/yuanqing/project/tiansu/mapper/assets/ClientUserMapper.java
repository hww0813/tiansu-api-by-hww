package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xucan on 2021-02-03 22:08
 * @author xucan
 */
@Repository
public interface ClientUserMapper extends BaseMapper<ClientUser,Long> {

    /**
     * 带排序条件的查询
     * @param t
     * @param orderStr
     * @return
     */
    List<ClientUser> getListWithOrder(@Param("clientUser") ClientUser clientUser, @Param("orderStr") String orderStr);


    /**
     * 批量确认状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);


    /**
     * 根据用户名list 查询用户
     * @param clientList
     * @return
     */
    List<ClientUser> getClientUserByUsername(@Param("clientList") List<Client> clientList, @Param("orderStr") String orderStr);
}
