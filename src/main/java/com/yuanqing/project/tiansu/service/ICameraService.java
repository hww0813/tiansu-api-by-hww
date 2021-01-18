package com.yuanqing.project.tiansu.service;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.Camera;
import com.yuanqing.project.tiansu.domain.ExternalDevice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 摄像头相关接口
 * Created by xucan on 2021-01-15 16:09
 * @author xucan
 */
public interface ICameraService extends BaseService<Camera,Long> {

    /**
     * 获取告警相关摄像头
     * @param filters 过滤
     * @return
     */
    List<Camera> findEventCameras(JSONObject filters);

    /**
     * 根据摄像头编码查询一个摄像头
     * @param deviceCode
     * @return 如果查出多个摄像头的情况，取第一个摄像头
     */
    Camera findOne(String deviceCode);

    /**
     * 判断camera在数据库中是否不存在
     * @param camera
     * @return true 不存在 / false 存在
     */
    boolean findCamera(Camera camera);

    /**
     * 统计当天更新过的 摄像头的状态
     * 查询依据为 摄像头表的update_time
     * @return 根据摄像头状态分组汇总
     */
    Map<Integer, Long> getCurrentStatus();


    /**
     * 获取当天活跃摄像头列表
     * TODO:oper表中 如果存放30天数据的话这个接口需要调整
     * @return
     */
    List<Camera> getActiveCamera();

    /**
     * 获取国标/非国标列表
     * TODO:这个接口 可以直接调用getList 获取国标和非国标
     * @param filter 过滤条件
     * @return
     */
    List<Camera> getNonNationalCameraList(JSONObject filter);

    /**
     * 查找子级摄像头
     * @param filters
     * @return
     */
    List<Camera> findChild(JSONObject filters);


    /**
     * 批量确认摄像头状态
     * @param list
     * @return
     */
    boolean changStatus(List<Camera> list);


    /**
     * 确认所有摄像头状态
     * @param
     * @return
     */
    boolean changAllStatus();

    /**
     * 获取首页国标、非国标编号摄像头
     *
     * TODO:重新写sql 直接查出数量
     * @return
     */
    Map<String, Long> getNonNationalCamera();


    /**
     * 格式化报表格式
     * TODO: 导出看能不能用ruoyi导出替换
     * @param filters
     * @return
     */
    List<JSONObject> getAllToReport(JSONObject filters);

    /**
     * 读取excel中的数据,生成list
     * @param file excel文件
     * TODO: 读取文件操作需要整合一下，没用过这个了 用的是导入外部设备表方法
     *
     */
    String readExcelFile(MultipartFile file);

    /**
     * 获取会话相关的摄像头列表
     * @param filters 过滤
     * @return
     */
    List<Camera> getSessionCameraList(JSONObject filters);


    /**
     * 获取被访问摄像头列表
     * @param filters 过滤
     * @return
     */
    List<Camera> visitedPage(JSONObject filters);

    /**
     * 读取外部设备表excel文件
     *
     * TODO: 读取文件操作需要整合一下
     * @param file
     * @return
     */
    String readExtExcelFile(MultipartFile file);


    /**
     * 导入外部设备
     *
     * TODO:逻辑有点复杂，需要细看
     * @param entity
     * @return
     */
    Long saveExternalDevice(ExternalDevice entity);
//

    /**
     *
     * @param entity
     * @param addList
     * @param updateList
     * @param cameraCodeMap
     */
    void dealCamera(ExternalDevice entity,List<Camera> addList,List<Camera> updateList,Map<String, Object> cameraCodeMap);

    /**
     * 批量插入
     * @param list
     */
    public void batchInsert(List<Camera> list);

    /**
     * 批量更新
     * @param list
     */
    void batchUpdate(List<Camera> list);


    /**
     * 获取下一个自增序列ID
     * @return
     */
    Long findId();

    /**
     * 根据摄像头国标 insert 到数据库和缓存中
     * TODO:和update方法一模一样 可以合并
     * @param camera
     */
    public void insert(Camera camera);

    /**
     * 根据摄像头国标 update 到数据库和缓存中
     * @param camera
     */
    public void update(Camera camera);

}
