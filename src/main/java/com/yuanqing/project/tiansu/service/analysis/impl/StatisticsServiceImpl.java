package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DoubleUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.analysis.VisitedRate;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.analysis.StatisticsMapper;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-01 14:44
 */

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private IMacsConfigService macsConfigService;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private StatisticsMapper statisticsMapper;


    /**
     * 1.根据regionID 查询区域信息
     * 2.查询区域下级地区信息
     * 3.统计数据库中的数据
     * 4.将统计的数据与下级地区关联
     * @param regionId 地区代码
     * @return
     */
    @Override
    public List<JSONObject> getVisitedRate(String regionId) {

        MacsRegion region = macsConfigService.getRegion(regionId);

        List<JSONObject> rateList = new ArrayList<>();

        if(region != null){
            log.info("获取配置中心-->地图配置成功,地区代码为:{} 地区名称为:{}",region.getId(),region.getName());

            List<MacsRegion> lowerRegion = macsConfigService.getLowerRegion(region.getId());

            if(CollectionUtils.isEmpty(lowerRegion)){

                log.error("获取下级地区配置失败,地区代码为:{} 地区名称为:{}",region.getId(),region.getName());
                return null;

            }else{
                log.info("获取下级地区配置成功");
                List<VisitedRate> visitedRateList = statisticsMapper.visitedRate();

                lowerRegion.stream().forEach(f ->{
                    JSONObject visitedRate = new JSONObject();
                    visitedRate.put("cityName",f.getName());
                    visitedRate.put("cityCode",f.getId());
                    visitedRateList.stream().forEach(h ->{
                        if(h.getRegionId() == Long.parseLong(f.getId())){
                            visitedRate.put("cameraCnt",h.getAllCount());
                            visitedRate.put("clientCnt",h.getTerminalCnt());
                            visitedRate.put("visitCnt",h.getVisitedCamera());
                            visitedRate.put("visitedCnt",h.getUserCnt());
                            Double rate =  DoubleUtils.roundOff(((double) h.getVisitedCamera()/(double) h.getAllCount()),2);
                            visitedRate.put("rate",rate+"%");
                        }else{
                            visitedRate.put("cameraCnt",0);
                            visitedRate.put("clientCnt",0);
                            visitedRate.put("visitCnt",0);
                            visitedRate.put("visitedCnt",0);
                            visitedRate.put("rate","0%");
                        }
                    });
                    rateList.add(visitedRate);
                });
                return rateList;
            }
        }
        return null;
    }

    @Override
    public List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit) {

        return statisticsMapper.getTerminalVisit(terminalVisit);
    }

    @Override
    public List<JSONObject> associateCameraInfo(List<OperationBehavior> operationBehaviorList) {

        List<Long> cameraIdList = operationBehaviorList.stream().map(f -> f.getCameraId()).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(cameraIdList)){
            log.error("cameraIdList为空");
            return null;
        }

        List<Camera> cameraList = cameraService.batchGetCameraById(cameraIdList);

        if(CollectionUtils.isEmpty(cameraList)){
            log.error("查询cameraList为空");
            return null;
        }

        List<JSONObject> terminalVisitedCameraList = new ArrayList<>();

        operationBehaviorList.stream().forEach(f ->{
            cameraList.stream().forEach(h -> {
                if(f.getCameraId() == h.getId()){
                    JSONObject j = new JSONObject();
                    j.put("deviceCode",h.getDeviceCode());
                    j.put("ipAddress",h.getIpAddress());
                    j.put("deviceName",h.getDeviceName());
                    j.put("port",h.getDomainPort());
                    j.put("action",f.getAction());
                    j.put("actionDetail",f.getActionDetail());
                    j.put("stamp",f.getStamp());
                    terminalVisitedCameraList.add(j);
                }
            });
        });

        return terminalVisitedCameraList;
    }


}
