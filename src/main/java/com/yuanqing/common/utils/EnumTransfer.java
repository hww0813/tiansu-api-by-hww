package com.yuanqing.common.utils;


/**
 * @author lvjingjing
 * @date 2021/7/20 16:25
 */
public class EnumTransfer {

    //用户状态转换
    public static String status(String status) {
        switch (status) {
            case "1":
                status = "新发现";
                break;
            case "2":
                status = "已确认";
                break;
            default:
                break;
        }
        return status;
    }

    //告警事件的事件状态转换
    public static String eventStatus(Integer status) {
        String type = null;
        switch (status) {
            case 0:
                type = "已确认";
                break;
            case 1:
                type = "新发现";
                break;
            default:
                break;
        }
        return type;
    }

    //检索结果图片库的来源转换
    public static String searchPictureSource(String type) {
        switch (type) {
            case "1":
                type = "依图人像";
                break;
            case "2":
                type = "以萨车踪";
                break;
            default:
                break;
        }
        return type;
    }


    //告警事件的动作转换
    public static String eventAction(String action) {
        switch (action) {
            case "0":
                action = "登录";
                break;
            case "1":
                action = "登出";
                break;
            case "2":
                action = "检索";
                break;
            default:
                break;
        }
        return action;
    }
}
