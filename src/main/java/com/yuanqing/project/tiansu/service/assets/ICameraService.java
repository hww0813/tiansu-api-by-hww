package com.yuanqing.project.tiansu.service.assets;

import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.ExternalDevice;

import java.util.List;
import java.util.Map;

/**
 * 摄像头相关接口
 * Created by xucan on 2021-01-15 16:09
 * @author xucan
 */

public interface ICameraService extends BaseService<Camera,Long> {


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
    List<Camera> getNonNationalCameraList(Camera camera);




    /**
     * 批量确认摄像头状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);


    /**
     * 确认所有摄像头状态
     * @param
     * @return
     */
    boolean changAllStatus();

    /**
     * 获取首页国标、非国标编号摄像头数量
     *
     * @return
     */
    Map<String, Long> getNonNationalCamera();





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
     * 根据ip更新摄像头,确定不是服务器
     * @param ipAddress
     */
    void updateIsNotServer(Long ipAddress);


}
