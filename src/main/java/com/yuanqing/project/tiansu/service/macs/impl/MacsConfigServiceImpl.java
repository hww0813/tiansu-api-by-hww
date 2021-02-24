package com.yuanqing.project.tiansu.service.macs.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 获取配置中心配置
 */
@Service
public class MacsConfigServiceImpl implements IMacsConfigService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MacsConfigServiceImpl.class);

    @Value("${tiansu.macshost}")
    private String prefix;

    private final String selectMacsConfigByTypeAndName_URL = "/configuration/config/selectMacsConfigByTypeAndName";

    private final String selectMacsRegionById_URL = "/configuration/region/";

    // TODO: 增加缓存。。。

    @Override
    public MacsConfig selectMacsConfigByTypeAndName(String type, String name) {
        if(StringUtils.isEmpty(type) || StringUtils.isEmpty(name)) {
            return null;
        }
        String rspStr = HttpUtils.sendGet(prefix+selectMacsConfigByTypeAndName_URL, "type=" + type + "&&name=" + name);
        if (StringUtils.isEmpty(rspStr))
        {
            LOGGER.error("获取配置异常 type={}, name={}", type, name);
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);
        if(jsonObject == null || !jsonObject.containsKey("data")) {  // || 200 != jsonObject.getIntValue("code")
            LOGGER.error("获取配置为空 type={}, name={}", type, name);
            return null;
        }

        JSONObject data = (JSONObject) jsonObject.get("data");
        MacsConfig macsConfig = data.toJavaObject(MacsConfig.class);

        return macsConfig;
    }


    @Override
    public MacsRegion selectMacsRegionById(String id) {
        if(id == null) {
            return null;
        }
        String rspStr = HttpUtils.sendGet(prefix+selectMacsRegionById_URL + id, "");
        if (StringUtils.isEmpty(rspStr))
        {
            LOGGER.error("获取区域异常 id={}", id);
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);
        if(jsonObject == null || !jsonObject.containsKey("data")) {  // || 200 != jsonObject.getIntValue("code")
            LOGGER.error("获取区域为空 id={}", id);
            return null;
        }

        JSONObject data = (JSONObject) jsonObject.get("data");
        MacsRegion macsRegion = data.toJavaObject(MacsRegion.class);

        return macsRegion;
    }

    @Override
    public String getRegion(String cityCode) {
        MacsRegion region = null;
        if (cityCode == null || cityCode == "") {
            MacsConfig macsConfig = selectMacsConfigByTypeAndName("system", "region");

            String[] regionCodeArr = macsConfig.getValue().split(",");
            String regionCode = regionCodeArr[regionCodeArr.length - 1];
            region = selectMacsRegionById(regionCode);
        } else {
            region = selectMacsRegionById(cityCode);
        }
        cityCode = "";
        if (region != null) {
            cityCode = region.getId().toString();
        }

        return cityCode;

    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") MacsConfig entity, SaveType type) {
        return null;
    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") MacsConfig entity) {
        return null;
    }

    @Override
    public void deleteById(@Valid @NotNull(message = "根据ID删除的ID不能为空") Long id) {

    }

    @Override
    public MacsConfig findById(@Valid @NotNull(message = "根据ID查询的ID不能为空") Long id) {
        return null;
    }

    @Override
    public List<MacsConfig> getList(MacsConfig macsConfig) {
        return null;
    }
}
