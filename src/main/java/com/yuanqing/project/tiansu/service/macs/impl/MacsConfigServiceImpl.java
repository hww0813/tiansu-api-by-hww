package com.yuanqing.project.tiansu.service.macs.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.queue.MacsMap;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.file.FileUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.feign.MacsFeignClient;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 获取配置中心配置
 * @author xucan
 */
@Service
public class MacsConfigServiceImpl implements IMacsConfigService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MacsConfigServiceImpl.class);

    @Value("${tiansu.macshost}")
    private String prefix;

    @Value("${tiansu.apiConfig}")
    private String apiConfig;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private MacsFeignClient macsFeignClient;


    private final String selectMacsRegionById_URL = "/tripartite/region/getConfigById";

    private final String selectMacsRegionInfo_URL = "/tripartite/region/regionInfo";

    private final String selectMacsRegionList_URL = "/tripartite/region/regionList";


    @PostConstruct
    public void init() {

        JSONObject localConfigJson = new JSONObject();

        try{
            localConfigJson = FileUtils.readFile(Paths.get(apiConfig));
        }catch (Exception e){
            LOGGER.error("读取本地配置文件出错！！");
            e.printStackTrace();
        }

        if(CollectionUtils.isEmpty(localConfigJson)){
            Set<String> keys = localConfigJson.keySet();

            JSONObject finalLocalConfigJson = localConfigJson;

            keys.stream().forEach(f ->{

                JSONArray jsonArray = finalLocalConfigJson.getJSONArray(f);

                List<MacsConfig> macsConfigList = jsonArray.toJavaList(MacsConfig.class);

                MacsMap.put(f,macsConfigList);

            });
            LOGGER.error("本地配置已写入缓存");
        }else{
            LOGGER.error("本地配置信息为空");
        }
    }

    public static final String MACS_TOKEN = "BearereyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImUzNjQ4YmI0LWQyMWEtNDRmZi05Mj" +
            "c0LWJjMDcxMDBjMzgzOSJ9.Ix7KKtW5Q4UZCbKgK5roz0y7xv7z5a_tb37k8alIwuAG9uiga6R6dBuCDEsx8HWqlUnXTVqNxHRaeo_6RY_e-w";



    @Override
    public List<MacsConfig> selectMacsConfigByTypeAndName(MacsConfig macsConfig) {
        if(macsConfig == null) {
            return null;
        }
        String macsKey = macsConfig.getType() + "-" + macsConfig.getName();

        String rspStr = null;

        try{

            rspStr =  macsFeignClient.getConfigById(macsConfig.getType(),macsConfig.getName());

        }catch (Exception e ){
            LOGGER.error("请求Macs接口异常,读取本地配置信息:"+macsKey);

            return getLocationConfig(macsKey);
        }

        if (StringUtils.isEmpty(rspStr)) {
            LOGGER.error("请求Macs接口返回结果为空,读取本地配置信息:"+macsKey);

            return getLocationConfig(macsKey);
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(rspStr);

        if(jsonObject == null || !jsonObject.containsKey("data")) {

            LOGGER.error("请求Macs接口配置为空,读取本地配置信息:" + macsKey);

            return getLocationConfig(macsKey);

        }else{

            LOGGER.info("请求Macs接口成功"+macsKey);

            JSONArray data = jsonObject.getJSONArray("data");

            List<MacsConfig> Config = data.toJavaList(MacsConfig.class);

            LOGGER.info("将配置信息写入缓存...");
            MacsMap.put(macsKey,Config);

            JSONObject MacsMapList = MacsMap.valueJson();

            LOGGER.info("将配置信息更新到本地配置文件...");
            FileUtils.writeFile(Paths.get(apiConfig),MacsMapList.toJSONString());

            return Config;
        }

    }

    private List<MacsConfig> getLocationConfig(String macsKey){

        JSONObject jsonObject = new JSONObject();

        try{

          jsonObject = FileUtils.readFile(Paths.get(apiConfig));

        }catch (Exception e){

        }

        JSONArray jsonArray = jsonObject.getJSONArray(macsKey);

        List<MacsConfig> config = jsonArray.toJavaList(MacsConfig.class);

        return config;
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

            redisCache.setCacheList(key,macsRegion,1);
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

    @Override
    public void setLowerRegionByCamera(List<Camera> list) {
        MacsRegion region = getRegion(null);

        //匹配所在区域
        List<MacsRegion> lowerRegion = getLowerRegion(region.getId());

        lowerRegion.stream().forEach(f -> {
            list.stream().forEach(h -> {

                if (h.getRegion() != null && h.getRegion().toString().equals(f.getId())) {
                    h.setRegionName(f.getName());
                }
            });
        });
    }

    @Override
    public AjaxResult selectMacsConfigById(Long id) {
        return macsFeignClient.selectMacsConfigById(id, MACS_TOKEN);
    }

    @Override
    public JSONObject selectMacsConfigList(MacsConfig macsConfig) {
        return macsFeignClient.getMacsConfigList(macsConfig, MACS_TOKEN);
    }

    @Override
    public AjaxResult insertMacsConfig(MacsConfig macsConfig) {
        return macsFeignClient.addMacsConfig(macsConfig, MACS_TOKEN);
    }

    @Override
    public AjaxResult updateMacsConfig(MacsConfig macsConfig) {
        return macsFeignClient.editMacsConfig(macsConfig, MACS_TOKEN);
    }

    @Override
    public AjaxResult deleteMacsConfigByIds(Long[] ids) {
        return macsFeignClient.removeMacsConfig(ids, MACS_TOKEN);
    }

//    @Override
//    public MacsConfig selectMacsConfigByTypeAndName(String type, String name) {
//        return macsConfigMapper.selectMacsConfigByTypeAndName(type,name);
//    }
}
