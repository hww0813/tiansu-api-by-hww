package com.yuanqing.project.tiansu.service.analysis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.FlowUtil;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.video.VisitRate;
import com.yuanqing.project.tiansu.mapper.analysis.VisitRateMapper;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VisitRateServiceImpl implements IVisitRateService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(VisitRateServiceImpl.class);

    @Resource
    private VisitRateMapper visitRateMapper;

    @Resource
    private IMacsConfigService macsConfigService;

//    private static Region region = new Region();

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
        String cityCode = macsConfigService.getRegion(filters.getString("cityCode"));
        filters.put("cityCode", cityCode);
        filters.put("codeLength", cityCode.length());
        List<VisitRate> list = new ArrayList<>();
        if (cityCode.length() == 6){
            filters.put("length", cityCode.length());
            list = visitRateMapper.getRateLastList(filters);
        } else {
            filters.put("length", cityCode.length() + 2);
            list = visitRateMapper.getRateList(filters);
        }
        LOGGER.error("===="+filters.toJSONString());
//        List<VisitRate> list = visitRateMapper.getRateList(filters);
        return new PageInfo<>(list);
    }


    @Override
    public List<VisitRate> getAllToReport(JSONObject filters) {
        String cityCode = macsConfigService.getRegion(filters.getString("cityCode"));
        filters.put("cityCode", cityCode);
        if (cityCode.length() == 6){
            filters.put("length", cityCode.length());
        } else {
            filters.put("length", cityCode.length() + 2);
        }
        List<VisitRate> list = visitRateMapper.getRateList(filters);
        for (VisitRate visitRate : list) {
            visitRate.setRate(this.setRate(visitRate.getRate()));
        }
        return list;
    }

    @Override
    public VisitRate getRegionRate(JSONObject filters) {
        String cityCode = macsConfigService.getRegion(filters.getString("cityCode"));
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
    public List<JSONObject> getRateCameraCntToReport(JSONObject filters) {
        List<Camera> cameraList = visitRateMapper.getCameraCntList(filters);
        List<JSONObject> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < cameraList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceCode", cameraList.get(i).getDeviceCode());
            if (cameraList.get(i).getDeviceName() == null) {
                jsonObject.put("deviceName", "");
            } else {
                jsonObject.put("deviceName", cameraList.get(i).getDeviceName());
            }
            if (cameraList.get(i).getIpAddress() != null) {
                jsonObject.put("ipAddress", IpUtils.longToIPv4(cameraList.get(i).getIpAddress()));
            } else {
                jsonObject.put("ipAddress", "");
            }
            if (cameraList.get(i).getRegion() != null && !cameraList.get(i).getRegion().equals("")) {
                if (cameraList.get(i).getRegion() != null) {
                    jsonObject.put("region", cameraList.get(i).getRegion());
                } else {
                    jsonObject.put("region", " ");
                }
            } else {
                jsonObject.put("region", " ");
            }
            if (cameraList.get(i).getManufacturer() == null) {
                jsonObject.put("manufacturer", "");
            } else {
                jsonObject.put("manufacturer", cameraList.get(i).getManufacturer());
            }
            jsonObject.put("status", cameraList.get(i).getStatus());
            if (cameraList.get(i).getUpdateTime() != null) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, cameraList.get(i).getUpdateTime()));
            } else {
                jsonObject.put("updateTime", "");
            }
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List<JSONObject> getRateVisitedCntToReport(JSONObject filters) {
        List<Camera> cameraList = visitRateMapper.getVisitedCntList(filters);
        List<JSONObject> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < cameraList.size(); i++) {
            if (cameraList.get(i) == null){
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            if (cameraList.get(i).getDeviceCode() == null) {
                jsonObject.put("deviceCode", "");
            } else {
                jsonObject.put("deviceCode", cameraList.get(i).getDeviceCode());
            }
            if (cameraList.get(i).getDeviceName() == null) {
                jsonObject.put("deviceName", "");
            } else {
                jsonObject.put("deviceName", cameraList.get(i).getDeviceName());
            }
            if (cameraList.get(i).getIpAddress() != null) {
                jsonObject.put("ipAddress", IpUtils.longToIPv4(cameraList.get(i).getIpAddress()));
            } else {
                jsonObject.put("ipAddress", "");
            }
            if (cameraList.get(i).getRegion() != null && !cameraList.get(i).getRegion().equals("")) {
                if (cameraList.get(i).getRegion() != null) {
                    jsonObject.put("region", cameraList.get(i).getRegion());
                } else {
                    jsonObject.put("region", " ");
                }
            } else {
                jsonObject.put("region", " ");
            }
            if (cameraList.get(i).getManufacturer() == null) {
                jsonObject.put("manufacturer", "");
            } else {
                jsonObject.put("manufacturer", cameraList.get(i).getManufacturer());
            }
            jsonObject.put("status", cameraList.get(i).getStatus());
            if (cameraList.get(i).getUpdateTime() != null) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, cameraList.get(i).getUpdateTime()));
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
                jsonObject.put("action", operList.get(i).getAction().getLabel());
            }
            if (operList.get(i).getActionDetail() != null && !operList.get(i).getActionDetail().equals("")) {
                jsonObject.put("actionDetail", operList.get(i).getActionDetail().getLabel());
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
            jsonObject.put("stamp", formatter.format(operList.get(i).getStamp()));
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public List<JSONObject> getRateClientCntToReport(JSONObject filters) {
        List<Client> clientList = visitRateMapper.getClientCntList(filters);
        List<JSONObject> list = new ArrayList<JSONObject>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < clientList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceCode", clientList.get(i).getDeviceCode());
            if (clientList.get(i).getIpAddress() != null && !clientList.get(i).getIpAddress().equals("")) {
                jsonObject.put("ipAddress", IpUtils.longToIPv4(clientList.get(i).getIpAddress()));
            }
            jsonObject.put("macAddress", clientList.get(i).getMacAddress());
            if (clientList.get(i).getUsername() != null && !clientList.get(i).getUsername().equals("")) {
                jsonObject.put("username", clientList.get(i).getUsername());
            } else {
                jsonObject.put("username", "");
            }
            jsonObject.put("status", clientList.get(i).getStatus());
            if (clientList.get(i).getUpdateTime() != null && !clientList.get(i).getUpdateTime().equals("")) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, clientList.get(i).getUpdateTime()));
            }
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public void updateStatisticsTable() {
        visitRateMapper.updateStatisticsTable();
    }

    private String setRate(String rate){
        rate = rate.replace("%","");
        if (Double.parseDouble(rate) > 100){
            return "100%";
        } else {
            return rate + "%";
        }
    }
}
