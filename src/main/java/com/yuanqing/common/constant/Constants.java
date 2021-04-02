package com.yuanqing.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 存储当前登录用户id的字段名
     */
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";

    /**
     * token有效期（小时）
     */
    public static final int TOKEN_EXPIRES_HOUR = 72;

    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHORIZATION = "authorization";

    /**
     * ehcache存放token的缓存库
     */
    public static final String TOKEN_CACHE_NAME = "token";

    /**
     * ehcache存放client的缓存库
     */
    public static final String CLIENT_CACHE_NAME = "CLIENT";

    /**
     * ehcache存放camera的缓存库
     */
    public static final String CAMERA_CACHE_NAME = "CAMERA";

    /**
     * ehcache存放session的缓存库
     */
    public static final String SESSION_CACHE_NAME = "OPER_SESSION";
    /**
     * ehcache存放session的缓存库
     */
    public static final String CAMERADTO_CACHE_NAME = "CAMERA_DTO";

    /**
     * ehcache存放config的缓存库
     */
    public static final String CONFIG_CACHE_NAME ="CONFIG";

    /**
     * ehcache存放session总量的缓存库
     */
    public static final String TOTAL_SESSION ="TOTAL_SESSION";

    /**
     * ehcache存放异常session的缓存库
     */
    public static final String UNUSUAL_SESSION ="UNUSUAL_SESSION";

    /**
     * ehcache存放IP blacklist的缓存库
     */
    public static final String BLACKLIST ="BLACKLIST";

    /**
     * ehcache存放forwardConfig的缓存库
     */
    public static final String FORWARD_CONFIG ="FORWARD_CONFIG";

    /**
     * ehcache存放syslogConfig的缓存库
     */
    public static final String SYSLOG_CONFIG = "SYSLOG_CONFIG";
    /**
     * ehcache存放serverTree的缓存库
     */
    public static final String SERVER_TREE ="SERVER_TREE";

    /**
     * ehcache存放USERNAME,SRC_CODE的缓存库
     */
    public static final String USERNAME = "USERNAME";
    public static final String SRC_CODE = "SRC_CODE";



    /**
     * ehcache存放platform的缓存库
     */
    public static final String PLATFORM = "PLATFORM";

    /**
     * ehcache存放登录失败的缓存库
     */
    public static final String FAILED_LOGIN = "FAILED_LOGIN";

    //生成的平台唯一标识
    public static String PLATFORM_KEY = "PLATFORM_KEY";

    public static String LOGIN_USER_NAME = "LOGIN_USER_NAME";

    public static final String TWO_FORMAT = "%s_%s";
    public static final String THREE_FORMAT = "%s_%s_%s";


    //redis缓存key值前缀

    public static final String CLIENT = "ts:Client";
    public static final String CAMERA = "ts:Camera";
    public static final String CLIENT_TERMINAL = "ts:ClientTerminal";


    //定时任务种类
    /** 摄像头被访问 -天 */
    public static final String VISIT_CAMERA_ONE_DAY = "VISIT_CAMERA_ONE_DAY";
    /** 摄像头被访问 -周 */
    public static final String VISIT_CAMERA_ONE_WEEK = "VISIT_CAMERA_ONE_WEEK";
    /** 摄像头被访问 -月 */
    public static final String VISIT_CAMERA_ONE_MONTH = "VISIT_CAMERA_ONE_MONTH";

    /** 摄像头被访问 -天 */
    public static final String PTZ_VISIT_CAMERA_ONE_DAY =  "PTZ_VISIT_CAMERA_ONE_DAY";
    /** 摄像头被访问 -周 */
    public static final String PTZ_VISIT_CAMERA_ONE_WEEK = "PTZ_VISIT_CAMERA_ONE_WEEK";
    /** 摄像头被访问 -月 */
    public static final String PTZ_VISIT_CAMERA_ONE_MONTH = "PTZ_VISIT_CAMERA_ONE_MONTH";

    /** 首页摄像头缓存KEY */
    public static final String INDEX_CLIENT_COUNTS_CACHE = "INDEX_CLIENT_COUNTS_CACHE";
    /** 首页摄用户缓存KEY */
    public static final String INDEX_USER_COUNTS_CACHE = "INDEX_USER_COUNTS_CACHE";

    public static final String INDEX_CAMERA_COUNTS_CACHE = "INDEX_CAMERA_COUNTS_CACHE";

    public static final String ALARM_CAMERA_COUNTS_CACHE = "ALARM_CAMERA_COUNTS_CACHE";

    public static final String INDEX_VISITED_RATE_CACHE = "INDEX_VISITED_RATE_CACHE";
    /** 首页区县缓存 月 */
    public static final String INDEX_VISITED_RATE_CACHE_MONTH = "INDEX_VISITED_RATE_CACHE_MONTH";
    /** 首页区县缓存 周 */
    public static final String INDEX_VISITED_RATE_CACHE_WEEK = "INDEX_VISITED_RATE_CACHE_WEEK";
    /** 首页区县缓存 日 */
    public static final String INDEX_VISITED_RATE_CACHE_DAY = "INDEX_VISITED_RATE_CACHE_DAY";
}
