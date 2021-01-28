package com.yuanqing.project.tiansu.service.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by xucan on 2021-01-19 17:49
 */
public interface IClientUserService extends BaseService<ClientUser,Long> {

    List<JSONObject> getAllToReport(JSONObject filters);

    boolean changStatus(List<ClientUser> list);

    /**
     * 读取excel中的数据,生成list
     */
    String readExcelFile(MultipartFile file);

    boolean findClientUser(String username);

    Long insertInto(ClientUser entity);

    Object getDistinctUser();
}
