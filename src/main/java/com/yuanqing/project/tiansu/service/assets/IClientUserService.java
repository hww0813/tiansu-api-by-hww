package com.yuanqing.project.tiansu.service.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by xucan on 2021-01-19 17:49
 * @author xucan
 */
public interface IClientUserService extends BaseService<ClientUser,Long> {

    List<ClientUser> getList(ClientUser clientUser, String orderStr);

    List<JSONObject> getAllToReport(JSONObject filters);

    /**
     * 批量确认状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);

    /**
     * 数据处理
     * 将用户列表列表根据client 关联出终端数并转换为dto对象
     * @param list 终端集合
     * @return 包含终端数的用户集合
     */
    List<ClientUserDto> handleClientUserTerminalNum(List<ClientUser> list);


    /**
     * client/clientUser同步删除
     * @param id
     * @param username
     */
    void delete(Long id,String username);

    /**
     * 读取excel中的数据,生成list
     */
    String readExcelFile(MultipartFile file);

    boolean findClientUser(String username);

    Long insert(ClientUser entity);

    /**
     * 获取活跃用户
     * @return
     */
    List<ClientUser> getActiveClientUser();


    /**
     * 根据用户名查询用户
     * @param clientList
     * @return
     */
    List<ClientUser> getClientUserByUsername(List<Client> clientList, String orderStr);
}
