package com.yuanqing.project.tiansu.config;

import java.util.regex.Pattern;

public interface IConstants {
    // 网卡
    public static final String CMD_LINUX_NETCARD = "ip addr |grep 'link/ether'";
    public static Pattern PATTERN_NETCARD = Pattern.compile("link/ether ([\\S]+)", Pattern.CASE_INSENSITIVE);

    // 消息交换机
    public static final String RABBITMQ_EXCHANGE = "exchange";

    // log的前缀
    public static final String LOG_PREFIX_ERROR_BASIC = "xxxxx ";
    public static final String LOG_PREFIX_INFO_BASIC = "***** ";

    // 消息队列的前缀
    public static final String RABBITMQ_QUEUE_PREFIX = "queue.";
    // 发送协议内容的队列
    public static final String RABBITMQ_QUEUE_PROTOCOL = String.format("%sprotocol", RABBITMQ_QUEUE_PREFIX);
    // 发送原始信令的队列
    public static final String RABBITMQ_QUEUE_RAW = String.format("%sraw", RABBITMQ_QUEUE_PREFIX);
    // 发送自审信息日志的队列
    public static final String RABBITMQ_QUEUE_SELFAUDIT = String.format("%sauditLog", RABBITMQ_QUEUE_PREFIX);
    // 发送系统日志的队列
    public static final String RABBITMQ_QUEUE_LOG = String.format("%ssystemctlLog", RABBITMQ_QUEUE_PREFIX);
    // 发送登录日志的队列
    public static final String RABBITMQ_QUEUE_LOGINLOG = String.format("%sloginLog", RABBITMQ_QUEUE_PREFIX);
   }
