package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import org.springframework.stereotype.Repository;

/**
 * Created by xucan on 2021-02-03 22:08
 * @author xucan
 */
@Repository
public interface ClientUserMapper extends BaseMapper<ClientUser,Long> {


    /**
     * 批量确认状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);
}
