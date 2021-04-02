package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xucan on 2021-01-15 16:31
 * @author xucan
 */
@Repository
public interface CameraMapper extends BaseMapper<Camera,Long> {

    /**
     * 带排序条件
     * @param camera
     * @return
     */
    List<Camera> getListWithOrder(@Param("camera") Camera camera, @Param("orderStr") String orderStr);

    /**
     * 获取摄像头国标和非国标数
     * @return
     */
    List<JSONObject> gourpByGb();

    /**
     * 更新摄像头不为服务器
     * @param ipAddress IP地址
     * @return
     */
    void updateIsNotServer(Long ipAddress);

    /**
     * 批量确认摄像头
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);

    /**
     * 根据camera列表 批量查询摄像头
     * @param cameraIdList
     * @return
     */
    List<Camera> batchGetCameraById(List<Long> cameraIdList);

    /**
     * 根据camera列表 批量查询摄像头
     * @param cameraCodeList
     * @return
     */
    List<Camera> batchGetCameraByCode(@Param("list") List<String> cameraCodeList,
                                      @Param("filter") Camera camera);

    /**
     * 确认所有摄像头
     * @return
     */
    boolean changAllStatus();

    List<Camera> findEventCameras(JSONObject jsonObject);

    /**
     * getList
     * 根据摄像头编码查询摄像头
     * @param deviceCode
     * @return
     */
//    List<Camera> findOne(@Param("deviceCode") String deviceCode);

    /**
     * getList
     * 判断camera在数据库中是否不存在
     * @param camera
     * @return
     */
//    Camera findCamera(Camera camera);

    /**
     *
     * TODO: ==
     * 查找子级摄像头
     * @param filters
     * @return
     */
//    List<Camera> findChild(JSONObject filters);

    /**
     * 批量确认摄像头状态
     * @param list
     * @return
     */
//    boolean changStatus(List<Camera> list);

    /**
     * 确认所有摄像头状态
     * @param
     * @return
     */
//    boolean changAllStatus();

    /**
     * getList
     * @return
     */
//    List<Camera> getGbList();


    /**
     * getList
     * @return
     */
//    List<Camera> getNgbList();

    /**
     * 获取会话相关的摄像头列表
     * @param filters 过滤
     * @return
     */
//    List<Camera> getSessionCameraList(JSONObject filters);

//    Camera maxId();

//    List<Camera> getAllVisited(JSONObject filters);

    /**
     * 获取总条数
     * @param filters
     * @return
     */
//    List<JSONObject> getTotal(JSONObject filters);

    /**
     * 获取物理实际总数
     */
//    List<JSONObject> getRealTotal();

    /**
     * 获取审计到的摄像头总数
     * @param filters
     * @return
     */
//    List<JSONObject> getProbeTotal(JSONObject filters);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    void batchInsert(@Param("list") List<Camera> list);

    /**
     * 批量更新
     * @param list
     */
    void batchUpdate(List<Camera> list);

    /**
     * 获取所有数据
     * @return
     */
//    List<Camera> getAllData();

    /**
     * 获得对应具有相同ip的摄像头，每种ip对应的摄像头只取一条数据
     * @return
     */
//    List<Long> getAllIp();

    /**
     * 根据ip更新摄像头,确定不是服务器
     * @param ipAddress
     */
//    void updateIsNotServer(Long ipAddress);

    /**
     * 获取下一个camera自增序列ID
     * @return
     */
//    Long findId();


//    public void insertCamera(Camera camera);


}
