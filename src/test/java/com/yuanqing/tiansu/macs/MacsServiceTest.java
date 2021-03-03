package com.yuanqing.tiansu.macs;

import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.domain.macs.MacsRegion;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-25 10:35
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MacsServiceTest {

    @Autowired
    private IMacsConfigService macsConfigService;


    /**
     * 获取macs配置中心配置
     */
    @Test
    public void selectMacsConfigByTypeAndName(){
        List<MacsConfig> macsConfig = macsConfigService.selectMacsConfigByTypeAndName(new MacsConfig("system","title"));
        System.out.println(macsConfig.get(0));
    }


    /**
     * 获取地区信息
     */
    @Test
    public void getRegion(){
        MacsRegion region = macsConfigService.getRegion(null);
        System.out.println(region);
    }

    /**
     * 获取下级地区信息
     */
    @Test
    public void selectMacsRegionById(){


        List<MacsRegion> regionList = macsConfigService.getLowerRegion("340000");

        System.out.println(regionList);
    }



}

