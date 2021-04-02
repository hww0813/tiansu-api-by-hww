package com.yuanqing.project.tiansu.service.authorize.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.mapper.assets.CameraMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientUserMapper;
import com.yuanqing.project.tiansu.service.authorize.IAuthorizeService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-01 10:14
 */
@Service
public class AuthorizeServiceImpl implements IAuthorizeService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeServiceImpl.class);

    @Autowired
    private IMacsConfigService macsConfigService;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private ClientUserMapper clientUserMapper;

    /**
     * 许可数
     */
    private static int cameraNum;

    private static int userNum;

    @PostConstruct
    public void initAuthorizeServiceImpl() {

        try {
            MacsConfig macsConfig = new MacsConfig();
            macsConfig.setName("permitCamera");
            macsConfig.setType("system");
            List<MacsConfig> cameraConfigList = macsConfigService.selectMacsConfigByTypeAndName(macsConfig);

            macsConfig.setName("permitUser");
            macsConfig.setType("system");
            List<MacsConfig> userConfigList = macsConfigService.selectMacsConfigByTypeAndName(macsConfig);

            MacsConfig permitCamera = cameraConfigList.get(0);
            MacsConfig permitUser = userConfigList.get(0);

            cameraNum = Integer.parseInt(permitCamera.getValue());
            userNum = Integer.parseInt(permitUser.getValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("摄像头许可数:{}用户许可书:{}",cameraNum,userNum);
    }


    /**
     * 认证许可数状态
     * @return -false:需要授权 -true:正常
     */
    @Override
    public boolean checkPermit() {

        /**
         * 统计数
         */
        int user = cameraMapper.getRealTotal();
        int camera = clientUserMapper.getRealTotal();

//        如果实际数量大于许可数量 返回false
        if (user > userNum || camera > cameraNum) {
            return false;
        }
        return true;
    }

    @Override
    public JSONObject replaceFilter(JSONObject filters) {

        boolean flag = checkPermit();
        int rowNum = cameraNum;

        if(!flag){
            if(filters == null){
                filters = new JSONObject();
            }
            filters.put("rownum",rowNum);
        }
        return filters;
    }

    @Override
    public JSONObject replaceUserFilter(JSONObject filters) {

        boolean flag = checkPermit();
        int rowNum = userNum;

        if(!flag){
            if(filters == null){
                filters = new JSONObject();
            }
            filters.put("rownum",rowNum);
        }
        return filters;
    }

    @Override
    public JSONObject getPermitNum() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("permitCamera",cameraNum);
        jsonObject.put("permitUser",userNum);
        return jsonObject;
    }
}
