package com.yuanqing.common.exception.config;

import com.yuanqing.common.exception.BaseException;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-08-17 16:37
 */
public class ConfigFileException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ConfigFileException(String content)
    {
        super(content);
    }
}
