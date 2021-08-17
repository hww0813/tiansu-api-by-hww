package com.yuanqing.common.exception.config;


/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-17 16:05
 */
public class ConfigParseException extends ConfigFileException {

    private static final long serialVersionUID = 1L;

    public ConfigParseException()
    {
        super("配置文件解析异常");
    }
}
