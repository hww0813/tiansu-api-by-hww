package com.yuanqing.project.tiansu.controller.analysis;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.analysis.TerminalVisit;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-03 11:02
 */

@RestController
@RequestMapping(value = "/api/analysis/client/visit")
@Api(value = "客户端访问接口", description = "客户端访问相关Api")
public class TerminalVisitedController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalVisitedController.class);

    @Autowired
    private IStatisticsService statisticsService;


    @GetMapping("/list")
    @ApiOperation(value = "获取客户端访问列表", httpMethod = "GET")
    public AjaxResult getClientVisitPage(@RequestParam(value = "startDate", required = false) String startDate,
                                         @RequestParam(value = "endDate", required = false) String endDate,
                                         @RequestParam(value = "clientIp", required = false) String clientIp,
                                         @RequestParam(value = "action", required = false) Integer action,
                                         @RequestParam(value = "user", required = false) String username) {
        TerminalVisit terminalVisit = new TerminalVisit();

        terminalVisit.setstartDate(startDate);
        terminalVisit.setendDate(endDate);
        terminalVisit.setIpAddress(IpUtils.ipToLong(clientIp));
        terminalVisit.setUsername(username);
        terminalVisit.setAction(action);


        startPage();
        List<TerminalVisit> visitList = statisticsService.getTerminalVisit(terminalVisit);

        return AjaxResult.success(getDataTable(visitList));

    }

}
