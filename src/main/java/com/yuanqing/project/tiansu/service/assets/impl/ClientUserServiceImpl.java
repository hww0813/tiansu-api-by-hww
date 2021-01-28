package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUser;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.assets.IClientUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by xucan on 2021-01-19 17:52
 * @author xucan
 */

@Service
public class ClientUserServiceImpl implements IClientUserService {

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        return null;
    }

    @Override
    public boolean changStatus(List<ClientUser> list) {
        return false;
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        return null;
    }

    @Override
    public boolean findClientUser(String username) {
        return false;
    }

    @Override
    public Long insertInto(ClientUser entity) {
        return null;
    }

    @Override
    public Object getDistinctUser() {
        return null;
    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") ClientUser entity) {
        return null;
    }

    @Override
    public void deleteById(@Valid @NotNull(message = "根据ID删除的ID不能为空") Long id) {

    }

    @Override
    public ClientUser findById(@Valid @NotNull(message = "根据ID查询的ID不能为空") Long id) {
        return null;
    }

    @Override
    public List<ClientUser> getList(ClientUser clientUser) {
        return null;
    }
}
