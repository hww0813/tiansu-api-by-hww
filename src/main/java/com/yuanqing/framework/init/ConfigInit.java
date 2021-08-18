package com.yuanqing.framework.init;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.exception.config.ConfigFileException;
import com.yuanqing.common.queue.MacsMap;
import com.yuanqing.common.utils.file.FileUtils;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-17 15:51
 */
@Component
public class ConfigInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInit.class);

    @Value("${tiansu.apiConfig}")
    private String apiConfig;

    @Value("${tiansu.default.system-failLoginCnt}")
    private String failLoginCnt;

    @Value("${tiansu.default.system-failedLoginTime}")
    private String failedLoginTime;

    @Value("${tiansu.default.system-permitCamera}")
    private String permitCamera;

    @Value("${tiansu.default.system-permitUser}")
    private String permitUser;

    @Autowired
    private IMacsConfigService macsConfigService;



    @PostConstruct
    public void init(){
        int count = 1;

        LOGGER.error("{}.配置初始化开始",count);
        count++;

        LOGGER.error("{}.获取用户登录失败次数:{}",count, Constants.SYSTEM_FAIL_LOGIN_CNT);
        count++;
        getMacsInfo(Constants.SYSTEM_FAIL_LOGIN_CNT,failLoginCnt);

        LOGGER.error("{}.获取用户登录限制时间:{}",count,Constants.SYSTEM_FAILED_LOGIN_TIME);
        count++;
        getMacsInfo(Constants.SYSTEM_FAILED_LOGIN_TIME,failLoginCnt);

        LOGGER.error("{}.获取摄像头许可数:{}",count,Constants.SYSTEM_PERMIT_CAMERA);
        count++;
        getMacsInfo(Constants.SYSTEM_PERMIT_CAMERA,permitCamera);

        LOGGER.error("{}.获取用户许可数:{}",count,Constants.SYSTEM_PERMIT_USER);
        count++;
        getMacsInfo(Constants.SYSTEM_PERMIT_USER,permitUser);

        JSONObject MacsMapList = MacsMap.valueJson();

        LOGGER.error("{}.正在写入配置文件",count);
        count++;
        FileUtils.writeFile(Paths.get(apiConfig),MacsMapList.toJSONString());

        LOGGER.error("{}.配置文件更新成功",count);
    }

    /**
     * 获取相关配置信息
     */
    private void getMacsInfo(String key, String defaultValue){
        String[] keys = key.split("-");
        MacsConfig macsConfig = new MacsConfig(keys[0],keys[1]);

        List<MacsConfig> macsConfigs;

        try{
            macsConfigs = macsConfigService.selectMacsConfigByTypeAndName(macsConfig);
        }catch (ConfigFileException e){
            LOGGER.error("读取本地配置异常,使用默认值:"+defaultValue);
            macsConfig.setValue(defaultValue);
            macsConfigs = new ArrayList<>();
            macsConfigs.add(macsConfig);
        }
            MacsMap.put(key,macsConfigs);
    }


}
