package com.yuanqing.project.tiansu.init;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component("commonInit")
public class CommonInit {
    @Resource
    private HttpConfigInit httpConfigInit;

    @PostConstruct
    public void init() throws Exception {
        //将所有初始化放在一个方法里
        httpConfigInit.run();
    }
}
