package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.DoubleUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.bean.BeanUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.analysis.VisitedRate;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.mapper.analysis.StatisticsMapper;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
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
    private IClientTerminalService terminalService;

    @Autowired
    private StatisticsMapper statisticsMapper;


    @Override
    public List<Statistics> getList(Statistics statistics) {

        return statisticsMapper.getList(statistics);
    }

    @Override
    public List<Statistics> getClientUserList(Statistics statistics) {

        return statisticsMapper.getClientUserList(statistics);
    }

    @Override
    public List<JSONObject> getClientList(Statistics statistics) {
        List<Statistics> statisticsList = statisticsMapper.getClientUserList(statistics);
        List<JSONObject> list = new ArrayList<>();
        statisticsList.stream().forEach(h -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("srcIp", IpUtils.longToIPv4(h.getSrcIp()));
            if (!StringUtils.isEmpty(h.getUsername())) {
                jsonObject.put("username", h.getUsername());
            } else {
                jsonObject.put("username", "");
            }
            jsonObject.put("count", h.getCount());
            list.add(jsonObject);
        });
        return list;
    }


    /**
     * 1.根据regionID 查询区域信息
     * 2.查询区域下级地区信息
     * 3.统计数据库中的数据
     * 4.将统计的数据与下级地区关联
     *
     * @param regionId 地区代码
     * @param dateType 时间属性
     * @return
     */
    @Override
    public List<JSONObject> getVisitedRate(String regionId, String dateType) {

        JSONObject filter = new JSONObject();

        //默认为当天
        switch (dateType) {
            default:
            case "day":
                filter = DateUtils.getDayTime();
                break;
            case "week":
                filter = DateUtils.getWeek();
                break;
            case "month":
                filter = DateUtils.getMonth();
                break;
            case "all":
                filter.put("startDate",null);
                filter.put("endDate",null);
                break;
        }

        MacsRegion region = macsConfigService.getRegion(regionId);

        List<JSONObject> rateList = new ArrayList<>();

        if (region != null) {
            log.info("获取配置中心-->地图配置成功,地区代码为:{} 地区名称为:{}", region.getId(), region.getName());

            //获取所有下级 把整个树都获取到   lowerRegion Map<String,List<String>>
            List<MacsRegion> cityRegion = macsConfigService.getLowerRegion(region.getId());

            if (CollectionUtils.isEmpty(cityRegion)) {

                log.error("获取下级地区配置失败,地区代码为:{} 地区名称为:{}", region.getId(), region.getName());
                return null;

            } else {
                log.info("1.获取省级下级地区配置成功,下级地区为:{}", cityRegion.toString());
                //遍历lowerRegion Map<String,List<String>> 将集合传入sql 一次性查出所有 去重之后的统计结果作为 Map.String的结果
                JSONObject finalFilter = filter;
                cityRegion.stream().forEach(f -> {

                    //获得该地区及所有下级区  eg:f为武汉市，获得武汉市的下级区
                    List<MacsRegion> areaRegion = macsConfigService.getLowerRegion(f.getId());
                    areaRegion.add(f);
                    List<String> list = areaRegion.stream().map(a -> a.getId()).collect(Collectors.toList());
                    log.info("2.获取市级下级地区配置成功,下级地区为:{}", list);

                    VisitedRate visitedRate = statisticsMapper.visitedRate(finalFilter, list);

                    //设置基本属性值，数量值初始化为0
                    JSONObject visited = new JSONObject();
                    visited.put("cityName", f.getName());
                    visited.put("cityCode", f.getId());
                    //摄像头总数
                    visited.put("cameraCnt", visitedRate.getAllCount() == null ? 0 : visitedRate.getAllCount());
                    //终端数
                    visited.put("clientCnt", visitedRate.getTerminalCnt() == null ? 0 : visitedRate.getTerminalCnt());
                    //摄像头被访问数
                    visited.put("visitedCnt", visitedRate.getVisitedCamera() == null ? 0 : visitedRate.getVisitedCamera());
                    //访问次数
                    visited.put("visitCnt", visitedRate.getVisitedCnt() == null ? 0 : visitedRate.getVisitedCnt());
                    Double rate = 0D;
                    if (visitedRate.getAllCount() != 0) {
                        rate = ((double) visitedRate.getVisitedCamera() / (double) visitedRate.getAllCount());
                    }

                    visited.put("rate", DoubleUtils.roundOff((rate * 100),2) + "%");
                    rateList.add(visited);

                });
                return rateList;
            }
        }
        return null;
    }

    @Override
    public List<TerminalVisit> getTerminalVisit(TerminalVisit terminalVisit, String orderStr) {

        return statisticsMapper.getTerminalVisit(terminalVisit, orderStr);
    }


    /**
     * @param cameraList 摄像头集合
     * @return 摄像头被访问对象
     */
    @Override
    public List<CameraVisit> getCameraVisit(List<Camera> cameraList, CameraVisit cameraVisit, String orderStr) {

        List<String> deviceCodeList = null;
        List<CameraVisit> cameraVisitList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cameraList)) {
            deviceCodeList = cameraList.stream().map(f -> f.getDeviceCode()).collect(Collectors.toList());
            cameraVisitList = statisticsMapper.getCameraVisit(deviceCodeList, cameraVisit, orderStr);
        }

        cameraVisitList.stream().forEach(f -> {
            if (!CollectionUtils.isEmpty(cameraList)) {
                cameraList.stream().forEach(h -> {
                    if (f.getDeviceCode().equals(h.getDeviceCode())) {
                        f.setDeviceName(h.getDeviceName());
                        f.setIpAddress(h.getIpAddress());
                        f.setRegionName(h.getRegionName());
                    }
                });
            } else {
                Camera camera = cameraService.findByCode(f.getDeviceCode());
                f.setDeviceName(camera.getDeviceName());
                f.setIpAddress(camera.getIpAddress());
                f.setRegionName(camera.getRegionName());
            }
        });
        return cameraVisitList;
    }

    /**
     * @param cameraList 获取摄像头被访问集合
     * @return 摄像头对象
     */
    @Override
    public List<String> getCameraVisited(List<Camera> cameraList, CameraVisit cameraVisit) {

        if (CollectionUtils.isEmpty(cameraList)) {
            log.error("cameraList为空，根据device_code批量查询摄像头列表为空");
            return null;
        }

        List<String> deviceCodeList = cameraList.stream().map(f -> f.getDeviceCode()).collect(Collectors.toList());

        List<String> cameraVisitList = statisticsMapper.getCameraVisited(deviceCodeList, cameraVisit);

        return cameraVisitList;
    }

    @Override
    public List<JSONObject> associateCameraInfo(List<OperationBehavior> operationBehaviorList) {

        List<Long> cameraIdList = operationBehaviorList.stream().map(f -> f.getCameraId()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cameraIdList)) {
            log.error("cameraIdList为空");
            return null;
        }

        List<Camera> cameraList = cameraService.batchGetCameraById(cameraIdList);

        if (CollectionUtils.isEmpty(cameraList)) {
            log.error("查询cameraList为空");
            return null;
        }

        List<JSONObject> terminalVisitedCameraList = new ArrayList<>();

        operationBehaviorList.stream().forEach(f -> {
            cameraList.stream().forEach(h -> {
                if (f.getCameraId().longValue() == h.getId().longValue()) {
                    JSONObject j = new JSONObject();
                    j.put("dstCode", h.getDeviceCode());
                    j.put("dstIp", h.getIpAddress());
                    j.put("dstDeviceName", h.getDeviceName());
                    j.put("dstPort", h.getDomainPort());
                    j.put("action", f.getAction());
                    j.put("actionDetail", f.getActionDetail());
                    j.put("stamp", f.getStamp());
                    j.put("callId", f.getCallId());
                    j.put("id", f.getId());
                    terminalVisitedCameraList.add(j);
                }
            });
        });

        return terminalVisitedCameraList;
    }


    @Override
    public List<JSONObject> associateTerminalInfo(List<OperationBehavior> operationBehaviorList) {

        List<Long> terminalIpList = operationBehaviorList.stream().map(f -> f.getSrcIp()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(terminalIpList)) {
            log.error("terminalIpList为空");
            return null;
        }

        List<ClientTerminal> terminalList = terminalService.getTerminalByIpList(terminalIpList, new ClientTerminal());

        if (CollectionUtils.isEmpty(terminalList)) {
            log.error("查询terminalList为空");
            return null;
        }

        List<JSONObject> cameraVisitedTerminalList = new ArrayList<>();

        operationBehaviorList.stream().forEach(f -> {
            terminalList.stream().forEach(h -> {
                if (f.getSrcIp().longValue() == h.getIpAddress().longValue()) {
                    JSONObject j = new JSONObject();
                    j.put("srcIp", h.getIpAddress());
                    j.put("srcPort", h.getDomainPort());
                    j.put("action", f.getAction());
                    j.put("actionDetail", f.getActionDetail());
                    j.put("stamp", f.getStamp());
                    j.put("username", f.getUsername());
                    j.put("callId", f.getCallId());
                    j.put("id", f.getId());
                    cameraVisitedTerminalList.add(j);
                }
            });
        });

        return cameraVisitedTerminalList;
    }

    @Override
    public List<Long> getTerminalVisited(List<Camera> cameraList, BaseEntity baseEntity) {

        if (CollectionUtils.isEmpty(cameraList)) {
            log.error("cameraList为空");
            return null;
        }

        List<String> deviceCodeList = cameraList.stream().map(f -> f.getDeviceCode()).collect(Collectors.toList());

        List<Long> terminalIpList = statisticsMapper.getTerminalVisited(deviceCodeList, baseEntity);

        return terminalIpList;
    }

    @Override
    public List<JSONObject> getClientVisitToReport(JSONObject filters) {
        List<JSONObject> clientVisitObjectList = new ArrayList<>();

        TerminalVisit condTerminalVisit = new TerminalVisit();
        condTerminalVisit.setstartDate(filters.getString("startDate"));
        condTerminalVisit.setendDate(filters.getString("endDate"));
        if (StringUtils.isNotEmpty(filters.getString("clientIp"))) {
            condTerminalVisit.setIpAddress(IpUtils.ipToLong(filters.getString("clientIp")));
        }
        condTerminalVisit.setUsername(filters.getString("user"));
        if (StringUtils.isNotEmpty(filters.getString("action"))) {
            condTerminalVisit.setAction(Integer.valueOf(filters.getString("action")));
        }
        // TODO: 这个地方没有传入排序参数
        List<TerminalVisit> terminalVisitList = statisticsMapper.getTerminalVisit(condTerminalVisit, null);
        if (!CollectionUtils.isEmpty(terminalVisitList)) {
            for (TerminalVisit terminalVisit : terminalVisitList) {
                JSONObject jsonObject = new JSONObject();
                // TODO: 都不显示终端编号
                jsonObject.put("CLIENT_CODE", "");
                if (terminalVisit.getIpAddress() != null) {
                    jsonObject.put("CLIENT_IP", IpUtils.longToIPv4(terminalVisit.getIpAddress()));
                } else {
                    jsonObject.put("CLIENT_IP", "");
                }
                if (StringUtils.isNotEmpty(terminalVisit.getUsername())) {
                    jsonObject.put("USERNAME", terminalVisit.getUsername());
                } else {
                    jsonObject.put("USERNAME", "");
                }
                Integer action = terminalVisit.getAction();
                if (0 == action) {
                    jsonObject.put("ACTION", "播放视频");
                } else if (1 == action) {
                    jsonObject.put("ACTION", "下载视频");
                } else if (2 == action) {
                    jsonObject.put("ACTION", "回放视频");
                } else if (3 == action) {
                    jsonObject.put("ACTION", "控制设备");
                } else if (4 == action) {
                    jsonObject.put("ACTION", "登录");
                } else if (5 == action) {
                    jsonObject.put("ACTION", "登出");
                } else if (6 == action) {
                    jsonObject.put("ACTION", "其他操作");
                } else if (7 == action) {
                    jsonObject.put("ACTION", "订阅");
                } else if (8 == action) {
                    jsonObject.put("ACTION", "通知");
                } else if (9 == action) {
                    jsonObject.put("ACTION", "查询");
                } else if (10 == action) {
                    jsonObject.put("ACTION", "PLAY_DEFEAT");
                } else {
                    jsonObject.put("ACTION", "OTHER");  // 其它
                }

                Long visitCnt = terminalVisit.getVisitCnt();
                if (visitCnt != null) {
                    jsonObject.put("VISIT_CNT", BigDecimal.valueOf(visitCnt));
                } else {
                    jsonObject.put("VISIT_CNT", BigDecimal.valueOf(0L));
                }

                Long cameraCnt = terminalVisit.getCameraCnt();
                if (cameraCnt != null) {
                    jsonObject.put("CAMERA_CNT", BigDecimal.valueOf(cameraCnt));
                } else {
                    jsonObject.put("CAMERA_CNT", BigDecimal.valueOf(0L));
                }

                clientVisitObjectList.add(jsonObject);
            }
        }
        return clientVisitObjectList;
    }

    @Override
    public List<JSONObject> getClientVisitCntToReport(JSONObject filters) {
        List<JSONObject> list = new ArrayList<>();

//        List<OperationBehavior> operList = statisticsMapper.getClientVisitCntList(filters);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        for (int i = 0; i < operList.size(); i++) {
//            JSONObject jsonObject = new JSONObject();
//            if (operList.get(i).getDstCode() != null && !operList.get(i).getDstCode().equals("")) {
//                jsonObject.put("dstCode", operList.get(i).getDstCode());
//            }
//            if (operList.get(i).getDstIp() != null && !operList.get(i).getDstIp().equals("")) {
//                jsonObject.put("dstIp", IpUtils.longToIPv4(operList.get(i).getDstIp()));
//            }
//            if (operList.get(i).getDstPort() != null && !operList.get(i).getDstPort().equals("")) {
//                jsonObject.put("dstPort", operList.get(i).getDstPort());
//            }
//            if (operList.get(i).getDstMac() != null && !operList.get(i).getDstMac().equals("")) {
//                jsonObject.put("dstMac", operList.get(i).getDstMac());
//            }
//            if (operList.get(i).getDstDeviceName() != null && !operList.get(i).getDstDeviceName().equals("")) {
//                jsonObject.put("dstDeviceName", operList.get(i).getDstDeviceName());
//            } else {
//                jsonObject.put("dstDeviceName", "");
//            }
//
//
//            if (operList.get(i).getUpFlow() != null && operList.get(i).getUpFlow() != 0L) {
//                jsonObject.put("upFlow", FlowUtil.setFlow(operList.get(i).getUpFlow()));
//            } else {
//                jsonObject.put("upFlow", "0");
//            }
//            if (operList.get(i).getDownFlow() != null && operList.get(i).getUpFlow() != 0L) {
//                jsonObject.put("downFlow", FlowUtil.setFlow(operList.get(i).getDownFlow()));
//            } else {
//                jsonObject.put("downFlow", "0");
//            }
//            if (operList.get(i).getAction() != null && !operList.get(i).getAction().equals("")) {
//                jsonObject.put("action", operList.get(i).getAction().getLabel());
//            }
//            if (operList.get(i).getActionDetail() != null && !operList.get(i).getActionDetail().equals("")) {
//                jsonObject.put("actionDetail", operList.get(i).getActionDetail().getLabel());
//            }
//            if (operList.get(i).getResult() != null && !operList.get(i).getResult().equals("")) {
//                if ("1".equals(operList.get(i).getResult())) {
//                    jsonObject.put("result", "成功");
//                } else if ("0".equals(operList.get(i).getResult())) {
//                    jsonObject.put("result", "失败");
//                } else {
//                    jsonObject.put("result", "未知");
//                }
//            } else {
//                jsonObject.put("result", "未知");
//            }
//            jsonObject.put("stamp", formatter.format(operList.get(i).getStamp()));
//            list.add(jsonObject);
//        }

        return list;
    }

    @Override
    public List<JSONObject> getClientVisitRelateCameraToReport(JSONObject filters) {
        Statistics condStatistics = new Statistics();
        condStatistics.setAction(filters.getInteger("action"));
        condStatistics.setDstCode(filters.getString("cameraCode"));
        condStatistics.setUsername(filters.getString("username"));
        condStatistics.setSrcIp(IpUtils.ipToLong(filters.getString("clientIp")));
        condStatistics.setstartDate(filters.getString("startDate"));
        condStatistics.setendDate(filters.getString("endDate"));

        Camera condCamera = new Camera();
        condCamera.setDeviceName(filters.getString("cameraName"));
        condCamera.setIpAddress(IpUtils.ipToLong(filters.getString("cameraIp")));
        condCamera.setRegion(filters.getInteger("region"));

        List<Statistics> statisticsList = this.getList(condStatistics);

        List<String> cameraCodeList = statisticsList.stream().map(f -> f.getDstCode()).collect(Collectors.toList());

        List<Camera> cameraList = cameraService.batchGetCameraByCode(cameraCodeList, condCamera);

//        List<CameraVisit> cameraVisitList = new ArrayList<>();

        List<JSONObject> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cameraList)) {
            cameraList.stream().forEach(f -> {

                Long sum = statisticsList.stream()
                        .filter(h -> h.getDstCode().equals(f.getDeviceCode()))
                        .mapToLong(h -> h.getCount())
                        .sum();

                JSONObject jsonObject = new JSONObject();
                if (f.getDeviceCode() != null) {
                    jsonObject.put("CAMERA_CODE", f.getDeviceCode());
                } else {
                    jsonObject.put("CAMERA_CODE", "");
                }
                if (f.getDeviceName() != null) {
                    jsonObject.put("CAMERA_NAME", f.getDeviceName());
                } else {
                    jsonObject.put("CAMERA_NAME", "");
                }
                if (f.getIpAddress() != null) {
                    jsonObject.put("CAMERA_IP", IpUtils.longToIPv4(f.getIpAddress()));
                } else {
                    jsonObject.put("CAMERA_IP", "");
                }
                if (f.getRegionName() != null) {
                    jsonObject.put("CAMERA_REGION", f.getRegionName());
                } else {
                    jsonObject.put("CAMERA_REGION", "");
                }
                if (sum != null) {
                    jsonObject.put("VISITED_CNT", sum.toString());
                } else {
                    jsonObject.put("VISITED_CNT", "0");
                }

                list.add(jsonObject);
            });
        }


//        List<JSONObject> list = clientVisitMapper.getClientVisitRelatedCameraList(filters);
//        for (JSONObject jsonObject : list) {
//            if (jsonObject.getString("CAMERA_NAME") == null) jsonObject.put("CAMERA_NAME", "");
//        }
        return list;
    }

    @Override
    public List<JSONObject> getCameraVisitedToReport(JSONObject filters) {
        List<JSONObject> cameraVisitedObjectList = new ArrayList<>();

        Camera condCamera = new Camera();
        // TODO: 开始结束时间未生效
//        condCamera.setstartDate(filters.getString("startDate"));
//        condCamera.setendDate(filters.getString("endDate"));
        if (StringUtils.isNotEmpty(filters.getString("cameraIp"))) {
            condCamera.setIpAddress(IpUtils.ipToLong(filters.getString("cameraIp")));
        }
        condCamera.setDeviceCode(filters.getString("cameraCode"));
        condCamera.setDeviceName(filters.getString("cameraName"));
        // TODO: 地区查询
//        condCamera.setRegion();
        List<Camera> cameraList = cameraService.getList(condCamera);

        CameraVisit condCameraVisit = new CameraVisit();
        condCameraVisit.setstartDate(filters.getString("startDate"));
        condCameraVisit.setendDate(filters.getString("endDate"));
        // TODO: 没有传入排序条件
        List<CameraVisit> cameraVisitList = this.getCameraVisit(cameraList, condCameraVisit, null);
        if (!CollectionUtils.isEmpty(cameraVisitList)) {
            for (CameraVisit cameraVisit : cameraVisitList) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("USERNAME", "");
                if (StringUtils.isNotEmpty(cameraVisit.getDeviceCode())) {
                    jsonObject.put("CAMERA_CODE", cameraVisit.getDeviceCode());
                } else {
                    jsonObject.put("CAMERA_CODE", "");
                }
                if (cameraVisit.getIpAddress() != null) {
                    jsonObject.put("CAMERA_IP", IpUtils.longToIPv4(cameraVisit.getIpAddress()));
                } else {
                    jsonObject.put("CAMERA_IP", "");
                }
                if (StringUtils.isNotEmpty(cameraVisit.getDeviceName())) {
                    jsonObject.put("CAMERA_NAME", cameraVisit.getDeviceName());
                } else {
                    jsonObject.put("CAMERA_NAME", "");
                }
                if (StringUtils.isNotEmpty(cameraVisit.getRegionName())) {
                    jsonObject.put("CAMERA_REGION", cameraVisit.getRegionName());
                } else {
                    jsonObject.put("CAMERA_REGION", "");
                }
                Integer action = cameraVisit.getAction();
                if (action != null) {
                    if (action == 0) {
                        jsonObject.put("ACTION", "播放视频");
                    } else if (action == 1) {
                        jsonObject.put("ACTION", "下载视频");
                    } else if (action == 2) {
                        jsonObject.put("ACTION", "回放视频");
                    } else if (action == 3) {
                        jsonObject.put("ACTION", "控制设备");
                    } else if (action == 4) {
                        jsonObject.put("ACTION", "登录");
                    } else if (action == 5) {
                        jsonObject.put("ACTION", "退出");
                    } else if (action == 6) {
                        jsonObject.put("ACTION", "其他操作");
                    } else if (action == 7) {
                        jsonObject.put("ACTION", "订阅");
                    } else if (action == 8) {
                        jsonObject.put("ACTION", "通知");
                    } else if (action == 9) {
                        jsonObject.put("ACTION", "查询");
                    } else if (action == 10) {
                        jsonObject.put("ACTION", "PLAY_DEFEAT");  // 播放失败
                    } else {
                        jsonObject.put("ACTION", "OTHER");  // 其它
                    }
                } else {
                    jsonObject.put("ACTION", "OTHER");  // 其它
                }

                Long visitCnt = cameraVisit.getVisitCnt();
                if (visitCnt != null) {
                    jsonObject.put("VISITED_CNT", BigDecimal.valueOf(visitCnt));
                } else {
                    jsonObject.put("VISITED_CNT", BigDecimal.valueOf(0L));
                }

                Long terminalCnt = cameraVisit.getTerminalCnt();
                if (terminalCnt != null) {
                    jsonObject.put("CLIENT_CNT", BigDecimal.valueOf(terminalCnt));
                } else {
                    jsonObject.put("CLIENT_CNT", BigDecimal.valueOf(0L));
                }

                cameraVisitedObjectList.add(jsonObject);
            }
        }
        return cameraVisitedObjectList;
    }
}
