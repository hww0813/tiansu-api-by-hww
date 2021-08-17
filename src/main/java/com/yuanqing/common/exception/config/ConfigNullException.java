package com.yuanqing.common.exception.config;

import com.yuanqing.common.exception.BaseException;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-17 16:05
 */
public class ConfigNullException extends ConfigFileException {

    private static final long serialVersionUID = 1L;

    public ConfigNullException()
    {
        super("配置文件信息为空");
    }
}
