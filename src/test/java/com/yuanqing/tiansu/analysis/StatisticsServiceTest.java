package com.yuanqing.tiansu.analysis;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.TokenConstants;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.feign.AlarmFeignClient;
import com.yuanqing.project.tiansu.service.feign.MacsFeignClient;
import com.yuanqing.project.tiansu.service.feign.PmcFeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void getVisitedRate() {
        List<JSONObject> visitedRate = statisticsService.getVisitedRate("340100", "day");
        System.out.println(visitedRate);

    }

    @Test
    public void getTerminalVisit() {
        TerminalVisit terminalVisit = new TerminalVisit();
        terminalVisit.setIpAddress(3232287076L);
        List<TerminalVisit> terminalVisitList = statisticsService.getTerminalVisit(terminalVisit, "camera_cnt desc");

        System.out.println(terminalVisitList);
    }

    @Resource
    private AlarmFeignClient alarmFeignClient;

    @Test
    public void test() {
//        String a = alarmFeignClient.getCameraId(1l);
//        System.out.println(a);
//        System.out.println(alarmFeignClient.getEventActive());
//        String a = alarmFeignClient.queryEventCntPerMinute(60);
//        System.out.println(a);

//        String a = alarmFeignClient.detailAndTOperUuid(1l);
//        System.out.println(a);

        List<Event> list = new ArrayList<>();
        Event event = new Event();
        event.setId(1l);
        list.add(event);
        event.setId(2l);
        list.add(event);
        event.setId(3l);
        list.add(event);
        List<Long> idList = list.stream().map(f->f.getId()).collect(Collectors.toList());
        Long []ids = new Long[list.size()];
        idList.toArray(ids);
        alarmFeignClient.confirm(ids);
    }

    @Resource
    private MacsFeignClient macsFeignClient;
    @Test
    public void test3(){
//        MacsConfig config = new MacsConfig();
//        config.setType("NonWork");
//        config.setName("stime");
//        String a = macsFeignClient.getConfigById(config);
//        System.out.println(a);
//        String b = macsFeignClient.getConfigById(2l);
//        System.out.println(b);
//        System.out.println(Long.parseLong("1"));
//        String b  = macsFeignClient.getInfo("110100");
//        String b = macsFeignClient.getList();
        Event event = new Event();
//        event.setEventCategory("敏感事件");
        event.setId(1l);
        String b = alarmFeignClient.listT(event,null,null);
        System.out.println(b);
    }

    @Resource
    private PmcFeignClient pmcFeignClient;
    @Test
    public void test4(){
        String a = pmcFeignClient.getConsulIp(TokenConstants.PMC_TOKEN);
        System.out.println(a);
    }
}
