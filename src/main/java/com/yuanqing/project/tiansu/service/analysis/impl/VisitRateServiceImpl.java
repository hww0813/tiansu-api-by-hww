package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.constant.DictConstants;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.FlowUtil;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.system.service.ISysDictDataService;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.report.VisitRate;
import com.yuanqing.project.tiansu.mapper.analysis.VisitRateMapper;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.yuanqing.common.constant.Constants.INDEX_VISITED_RATE_CACHE;

@Service
@Transactional(readOnly = true)
public class VisitRateServiceImpl implements IVisitRateService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(VisitRateServiceImpl.class);

    @Resource
    private VisitRateMapper visitRateMapper;

    @Resource
    private IMacsConfigService macsConfigService;

    @Resource
    private ISysDictDataService sysDictDataService;
//    private static Region region = new Region();

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") VisitRate entity, SaveType type) {
        return null;
    }

    @Override
    public Long save(VisitRate entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public VisitRate findById(Long id) {
        return null;
    }

    @Override
    public List<VisitRate> getList(VisitRate visitRate) {
        return null;
    }

    @Override
    public PageInfo<VisitRate> page(int pageNum, int pageSize, JSONObject filters) {
        PageHelper.startPage(pageNum, pageSize);
        String cityCode = macsConfigService.getRegion(filters.getString("cityCode")).getId();
        filters.put("cityCode", cityCode);
        filters.put("codeLength", cityCode.length());
        List<VisitRate> list = new ArrayList<>();
        if (cityCode.length() == 6) {
            filters.put("length", cityCode.length());
            list = visitRateMapper.getRateLastList(filters);
        } else {
            filters.put("length", cityCode.length() + 2);
            list = visitRateMapper.getRateList(filters);
        }
        LOGGER.error("====" + filters.toJSONString());
//        List<VisitRate> list = visitRateMapper.getRateList(filters);
        return new PageInfo<>(list);
    }


    @Override
    public List<VisitRate> getAllToReport(JSONObject filters) {
//        String cityCode = macsConfigService.getRegion(filters.getString("cityCode")).getId();
//        filters.put("cityCode", cityCode);
//        if (cityCode.length() == 6){
//            filters.put("length", cityCode.length());
//        } else {
//            filters.put("length", cityCode.length() + 2);
//        }

        List<VisitRate> visitRateList = new ArrayList<>();

        String time = DateUtils.getTimeType((LocalDate) filters.get("startDate"), (LocalDate) filters.get("endDate"));
        List<JSONObject> all = redisCache.getCacheObject(INDEX_VISITED_RATE_CACHE + "_" + time);
        if (!CollectionUtils.isEmpty(all)) {
            for (JSONObject visitRateObj : all) {
                Long cityCode = 0L;
                String cityCodeStr = visitRateObj.getString("cityCode");
                if (StringUtils.isNotEmpty(cityCodeStr)) {
                    cityCode = Long.parseLong(cityCodeStr);
                }
                Long cameraCnt = 0L;
                if (visitRateObj.getInteger("cameraCnt") != null) {
                    cameraCnt = visitRateObj.getInteger("cameraCnt").longValue();
                }
                Long visitCnt = 0L;
                if (visitRateObj.getInteger("visitCnt") != null) {
                    visitCnt = visitRateObj.getInteger("visitCnt").longValue();
                }
                Long clientCnt = 0L;
                if (visitRateObj.getInteger("clientCnt") != null) {
                    clientCnt = visitRateObj.getInteger("clientCnt").longValue();
                }
                Long visitedCnt = 0L;
                if (visitRateObj.getInteger("visitedCnt") != null) {
                    visitedCnt = visitRateObj.getInteger("visitedCnt").longValue();
                }
                VisitRate visitRate = new VisitRate(cityCode,
                        visitRateObj.getString("cityName"),
                        cameraCnt,
                        visitRateObj.getString("rate"),
                        visitCnt,
                        clientCnt,
                        visitedCnt);
                visitRateList.add(visitRate);
            }
        }
        return visitRateList;
    }

    @Override
    public VisitRate getRegionRate(JSONObject filters) {
        String cityCode = macsConfigService.getRegion(filters.getString("cityCode")).getId();
        filters.put("cityCode", cityCode);
        filters.put("length", cityCode.length());
        List<VisitRate> list = visitRateMapper.getRegionRate(filters);
        LOGGER.error("filters ====@ " + JSON.toJSONString(filters) + "list ====@ " + JSON.toJSONString(list));
        if (list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PageInfo<Camera> getCameraCntList(int pageNum, int pageSize, JSONObject filters) {
        PageHelper.startPage(pageNum, pageSize);
        List<Camera> list = visitRateMapper.getCameraCntList(filters);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Camera> getVisitedCntList(int pageNum, int pageSize, JSONObject filters) {
        PageHelper.startPage(pageNum, pageSize);
        List<Camera> list = visitRateMapper.getVisitedCntList(filters);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<OperationBehavior> getVisitCntList(int pageNum, int pageSize, JSONObject filters) {
        PageHelper.startPage(pageNum, pageSize);
        List<OperationBehavior> list = visitRateMapper.getVisitCntList(filters);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Client> getClientCntList(int pageNum, int pageSize, JSONObject filters) {
        PageHelper.startPage(pageNum, pageSize);
        List<Client> list = visitRateMapper.getClientCntList(filters);
        return new PageInfo<>(list);
    }

    @Override
    public List<JSONObject> getRateCameraCntToReport(Camera ca) {
        List<Camera> cameraList = cameraService.getAllList(ca);

        macsConfigService.setLowerRegionByCamera(cameraList);

        List<JSONObject> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cameraList)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Camera camera : cameraList) {
                JSONObject jsonObject = new JSONObject();
                if (camera.getDeviceCode() == null) {
                    jsonObject.put("deviceCode", "");
                } else {
                    jsonObject.put("deviceCode", camera.getDeviceCode());
                }
                if (camera.getDeviceName() != null) {
                    jsonObject.put("deviceName", camera.getDeviceName());
                } else {
                    jsonObject.put("deviceName", "");
                }
                if (camera.getIpAddress() != null) {
                    jsonObject.put("ipAddress", IpUtils.longToIPv4(camera.getIpAddress()));
                } else {
                    jsonObject.put("ipAddress", "");
                }
                if (StringUtils.isNotEmpty(camera.getRegionName())) {
                    jsonObject.put("region", camera.getRegionName());
                } else {
                    jsonObject.put("region", " ");
                }
                if (camera.getManufacturer() != null) {
                    jsonObject.put("manufacturer", camera.getManufacturer());
                } else {
                    jsonObject.put("manufacturer", "");
                }
                if (camera.getStatus() != null && 0 == camera.getStatus()) {
                    jsonObject.put("status", "已确认");
                } else if (camera.getStatus() == null || 1 == camera.getStatus()) {
                    jsonObject.put("status", "新发现");
                }
                if (camera.getUpdateTime() != null) {
                    jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, camera.getUpdateTime()));
                } else {
                    jsonObject.put("updateTime", "");
                }
                list.add(jsonObject);
            }
        }

        return list;
    }

    @Override
    public List<JSONObject> getRateVisitedCntToReport(JSONObject filters) {
        CameraVisit condCameraVisit = new CameraVisit();
        condCameraVisit.setstartDate(filters.getString("startDate"));
        condCameraVisit.setendDate(filters.getString("endDate"));

        Camera condCamera = new Camera();
        condCamera.setstartDate(filters.getString("startDate"));
        condCamera.setendDate(filters.getString("endDate"));
        condCamera.setIpAddress(IpUtils.ipToLong(filters.getString("ipAddress")));
        condCamera.setRegion(filters.getInteger("region"));

        List<Camera> cameraList = cameraService.getList(condCamera);
        List<String> cameraCodeList = statisticsService.getCameraVisited(cameraList, condCameraVisit);
        List<Camera> finalCameraList = cameraService.batchGetCameraByCode(cameraCodeList, new Camera());

//        List<Camera> cameraList = visitRateMapper.getVisitedCntList(filters);
        List<JSONObject> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Camera camera : finalCameraList) {
            if (camera == null) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            if (camera.getDeviceCode() != null) {
                jsonObject.put("deviceCode", camera.getDeviceCode());
            } else {
                jsonObject.put("deviceCode", "");
            }
            if (camera.getDeviceName() != null) {
                jsonObject.put("deviceName", camera.getDeviceName());
            } else {
                jsonObject.put("deviceName", "");
            }
            if (camera.getIpAddress() != null) {
                jsonObject.put("ipAddress", IpUtils.longToIPv4(camera.getIpAddress()));
            } else {
                jsonObject.put("ipAddress", "");
            }
            if (StringUtils.isNotEmpty(camera.getRegionName())) {
                jsonObject.put("region", camera.getRegionName());
            } else {
                jsonObject.put("region", " ");
            }
            if (camera.getManufacturer() != null) {
                jsonObject.put("manufacturer", camera.getManufacturer());
            } else {
                jsonObject.put("manufacturer", "");
            }
            if (camera.getStatus() != null && 0 == camera.getStatus()) {
                jsonObject.put("status", "已确认");
            } else if (camera.getStatus() == null || 1 == camera.getStatus()) {
                jsonObject.put("status", "新发现");
            }
            if (camera.getUpdateTime() != null) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, camera.getUpdateTime()));
            } else {
                jsonObject.put("updateTime", "");
            }
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List<JSONObject> getRateVisitCntToReport(JSONObject filters) {
        List<OperationBehavior> operList = visitRateMapper.getVisitCntList(filters);
        List<JSONObject> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < operList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            if (operList.get(i).getSrcIp() != null && !operList.get(i).getSrcIp().equals("")) {
                jsonObject.put("srcIp", IpUtils.longToIPv4(operList.get(i).getSrcIp()));
            }
            if (operList.get(i).getDstIp() != null && !operList.get(i).getDstIp().equals("")) {
                jsonObject.put("dstIp", IpUtils.longToIPv4(operList.get(i).getDstIp()));
            }
            if (operList.get(i).getDstCode() != null && !operList.get(i).getDstCode().equals("")) {
                jsonObject.put("dstCode", operList.get(i).getDstCode());
            }

            if (operList.get(i).getDstDeviceName() != null && !operList.get(i).getDstDeviceName().equals("")) {
                jsonObject.put("dstDeviceName", operList.get(i).getDstDeviceName());
            } else {
                jsonObject.put("dstDeviceName", "");
            }

            if (operList.get(i).getUsername() != null && !operList.get(i).getUsername().equals("")) {
                jsonObject.put("username", operList.get(i).getUsername());
            } else {
                jsonObject.put("username", "");
            }


            if (operList.get(i).getUpFlow() != null && operList.get(i).getUpFlow() != 0L) {
                jsonObject.put("upFlow", FlowUtil.setFlow(operList.get(i).getUpFlow()));
            } else {
                jsonObject.put("upFlow", "0");
            }
            if (operList.get(i).getDownFlow() != null && operList.get(i).getUpFlow() != 0L) {
                jsonObject.put("downFlow", FlowUtil.setFlow(operList.get(i).getDownFlow()));
            } else {
                jsonObject.put("downFlow", "0");
            }
            if (operList.get(i).getAction() != null && !operList.get(i).getAction().equals("")) {

                String action = sysDictDataService.selectDictLabel(DictConstants.OPERATION_ACTION, operList.get(i).getAction());
                jsonObject.put("action", action);
            }
            if (operList.get(i).getActionDetail() != null && !operList.get(i).getActionDetail().equals("")) {
                String actionDetail = sysDictDataService.selectDictLabel(DictConstants.OPERATION_ACTION_DETAIL, operList.get(i).getAction());
                jsonObject.put("actionDetail", actionDetail);
            }
            if (operList.get(i).getResult() != null && !operList.get(i).getResult().equals("")) {
                if ("1".equals(operList.get(i).getResult())) {
                    jsonObject.put("result", "成功");
                } else if ("0".equals(operList.get(i).getResult())) {
                    jsonObject.put("result", "失败");
                } else {
                    jsonObject.put("result", "未知");
                }
            } else {
                jsonObject.put("result", "未知");
            }
            jsonObject.put("stamp", operList.get(i).getStamp());
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List<JSONObject> getRateClientCntToReport(JSONObject filters) {
        Camera condCamera = new Camera();
        condCamera.setRegion(filters.getInteger("region"));

        ClientTerminal condClientTerminal = new ClientTerminal();
        condClientTerminal.setStatus(filters.getInteger("status"));
        condClientTerminal.setIpAddress(IpUtils.ipToLong(filters.getString("ipAddress")));
        condClientTerminal.setstartDate(filters.getString("startDate"));
        condClientTerminal.setendDate(filters.getString("endDate"));

        List<Camera> cameraList = cameraService.getList(condCamera);
        List<Long> terminalIpList = statisticsService.getTerminalVisited(cameraList, condClientTerminal);
        List<ClientTerminal> clientTerminalList = clientTerminalService.getTerminalByIpList(terminalIpList, condClientTerminal);

//        List<Client> clientList = visitRateMapper.getClientCntList(filters);
        List<JSONObject> list = new ArrayList<JSONObject>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (ClientTerminal clientTerminal : clientTerminalList) {
            JSONObject jsonObject = new JSONObject();
//            if(clientTerminal.getDeviceCode() != null) {
//                jsonObject.put("deviceCode", clientTerminal.getDeviceCode());
//            } else {
//                jsonObject.put("deviceCode", "");
//            }
            if (clientTerminal.getIpAddress() != null) {
                jsonObject.put("ipAddress", IpUtils.longToIPv4(clientTerminal.getIpAddress()));
            } else {
                jsonObject.put("ipAddress", "");
            }
            if (clientTerminal.getMacAddress() != null) {
                jsonObject.put("macAddress", clientTerminal.getMacAddress());
            } else {
                jsonObject.put("macAddress", "");
            }
            if (clientTerminal.getStatus() != null && 0 == clientTerminal.getStatus()) {
                jsonObject.put("status", "已确认");
            } else if (clientTerminal.getStatus() == null || 1 == clientTerminal.getStatus()) {
                jsonObject.put("status", "新发现");
            }
            if (clientTerminal.getUpdateTime() != null) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, clientTerminal.getUpdateTime()));
            } else {
                jsonObject.put("updateTime", "");
            }
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public void updateStatisticsTable() {
        visitRateMapper.updateStatisticsTable();
    }

    private String setRate(String rate) {
        rate = rate.replace("%", "");
        if (Double.parseDouble(rate) > 100) {
            return "100%";
        } else {
            return rate + "%";
        }
    }
}
