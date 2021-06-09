package com.yuanqing.project.tiansu.service.macs.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取配置中心配置
 * @author xucan
 */
@Service
public class MacsConfigServiceImpl implements IMacsConfigService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MacsConfigServiceImpl.class);

    @Value("${tiansu.macshost}")
    private String prefix;

    @Autowired
    private RedisCache redisCache;

    private final String getConfigList_URL = "/tripartite/config/getConfigList";

    private final String selectMacsRegionById_URL = "/tripartite/region/getConfigById";

    private final String selectMacsRegionInfo_URL = "/tripartite/region/regionInfo";

    private final String selectMacsRegionList_URL = "/tripartite/region/regionList";

    // TODO: 增加缓存。。。

    @Override
    public List<MacsConfig> selectMacsConfigByTypeAndName(MacsConfig macsConfig) {
        if(macsConfig == null) {
            return null;
        }

        String rspStr = HttpUtils.sendGet(prefix+getConfigList_URL, macsConfig.toParamsString());
        if (StringUtils.isEmpty(rspStr))
        {
            LOGGER.error("获取配置异常"+macsConfig.toParamsString());
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);
        if(jsonObject == null || !jsonObject.containsKey("data")) {
            LOGGER.error("获取配置为空"+macsConfig.toParamsString());
            return null;
        }

        JSONArray data = jsonObject.getJSONArray("data");

        List<MacsConfig> Config = data.toJavaList(MacsConfig.class);

        return Config;
    }


    /**
     * 获取下级地区
     * @param id
     * @return
     */
    @Override
    public List<MacsRegion> getLowerRegion(String id) {
        if(StringUtils.isEmpty(id)) {
            return null;
        }

        String key = Constants.CONFIG_LOWER_REGION + id;

        List<Object> redisConfig = redisCache.getCacheList(key);

        if(CollectionUtils.isEmpty(redisConfig)){
            String rspStr = HttpUtils.sendGet(prefix+selectMacsRegionById_URL, "regionId="+id);
            if (StringUtils.isEmpty(rspStr))
            {
                LOGGER.error("获取区域异常 id={}", id);
                return null;
            }
            JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);

            // || 200 != jsonObject.getIntValue("code")
            if(jsonObject == null || !jsonObject.containsKey("data")) {
                LOGGER.error("获取区域为空 id={}", id);
                return null;
            }
            JSONArray data = jsonObject.getJSONArray("data");
            List<MacsRegion> macsRegion = data.toJavaList(MacsRegion.class);

            redisCache.setCacheList(key,macsRegion);
            LOGGER.info("下级地区缓存更新成功");
            return macsRegion;
        }else{
            List<MacsRegion> lowerRegion =(List<MacsRegion>)(Object)redisConfig;
            return lowerRegion;
        }
    }

    /**
     * 根据regionId 获取区域详情
     * @param id
     * @return
     */
    @Override
    public MacsRegion selectMacsRegionInfo(String id) {
        if(StringUtils.isEmpty(id)) {
            return null;
        }
        String rspStr = HttpUtils.sendGet(prefix+selectMacsRegionInfo_URL, "regionId="+id);
        if (StringUtils.isEmpty(rspStr))
        {
            LOGGER.error("获取区域异常 id={}", id);
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);

        // || 200 != jsonObject.getIntValue("code")
        if(jsonObject == null || !jsonObject.containsKey("data")) {
            LOGGER.error("获取区域为空 id={}", id);
            return null;
        }

        JSONObject data = (JSONObject) jsonObject.get("data");
        MacsRegion macsRegion = data.toJavaObject(MacsRegion.class);

        return macsRegion;
    }

    @Override
    public MacsRegion getRegion(String cityCode) {
        MacsRegion region;
        if (cityCode == null || "".equals(cityCode)) {

            List<MacsConfig> macsConfig = selectMacsConfigByTypeAndName(new MacsConfig("system","region"));

            if(CollectionUtils.isEmpty(macsConfig)){
                LOGGER.error("获取区域异常");
                return null;
            }

            String[] regionCodeArr = macsConfig.get(0).getValue().split(",");
            String regionCode = regionCodeArr[regionCodeArr.length - 1];
            region = selectMacsRegionInfo(regionCode);
        } else {
            region = selectMacsRegionInfo(cityCode);
        }
        return region;

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

    @Override
    public List<MacsRegion> getRegionList() {

        String rspStr = HttpUtils.sendGet(prefix+selectMacsRegionList_URL,"");

        if (StringUtils.isEmpty(rspStr))
        {
            LOGGER.error("获取区域异常");
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);

        // || 200 != jsonObject.getIntValue("code")
        if(jsonObject == null || !jsonObject.containsKey("data")) {
            LOGGER.error("获取区域为空");
            return null;
        }

        JSONArray data = jsonObject.getJSONArray("data");
        List<MacsRegion> macsRegion = data.toJavaList(MacsRegion.class);
        return macsRegion;
    }

    @Override
    public List<String> getAllLowerRegion(String regionId) {
        //下级区也需要查询,将所有本级及下级平台的id存入string
        List<MacsRegion> areaRegion = getLowerRegion(regionId);
        List<String> region = new ArrayList<>();
        region.add(regionId);
        areaRegion.stream().forEach(r->{
            region.add(r.getId());
        });
        return  region;
    }
}
