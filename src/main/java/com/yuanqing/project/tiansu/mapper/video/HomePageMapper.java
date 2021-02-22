package com.yuanqing.project.tiansu.mapper.video;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.video.CameraStatistics;
import com.yuanqing.project.tiansu.domain.video.StatisticsSearch;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author dingyong
 * @date 2019/6/22 - 16:46
 */
@Repository
public interface HomePageMapper {

    /**
     * @author: dongchao
     * @create: 2021/2/4-22:47
     * @description: 统计一天访问最多的摄像头
     * @param: startTime：开始时间, endTime：结束时间 ， size查询limit
     * @return:
     */
    List<CameraStatistics> getCameraStatisticsByTime(Date startTime , Date endTime ,int size ,Long action,String ascFlag);


    List<JSONObject> getClinetStatisticsByTime(StatisticsSearch statisticsSearch);

    List<JSONObject> getUserStatisticsByTime(StatisticsSearch statisticsSearch);

}
