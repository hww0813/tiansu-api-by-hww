package com.yuanqing.tiansu.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-02 15:31
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticsServiceTest {

    @Autowired
    private IStatisticsService statisticsService;


    /**
     * 根据地区代码查询下级区域摄像头访问率
     */
    @Test
    public void getVisitedRate(){
        List<JSONObject> visitedRate = statisticsService.getVisitedRate("340100","day");
        System.out.println(visitedRate);

    }

    @Test
    public void getTerminalVisit(){
        TerminalVisit terminalVisit = new TerminalVisit();
        terminalVisit.setIpAddress(3232287076L);
        List<TerminalVisit> terminalVisitList = statisticsService.getTerminalVisit(terminalVisit, "camera_cnt desc");

        System.out.println(terminalVisitList);
    }

}
