package com.yuanqing.common.exception.config;


/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-17 16:05
 */
public class ConfigParseException extends ConfigFileException {

    private static final long serialVersionUID = 1L;

    public ConfigParseException(String ConfigParams){
        super("config.params.read.error",ConfigParams);
    }
}
