package com.yuanqing.framework.web.domain;

import com.yuanqing.common.constant.HttpStatus;
import com.yuanqing.common.utils.StringUtils;

import java.util.HashMap;

/**
 * 用于承接老系统使用pagehelper 返回案例
 *
 * @author dongchao
 */
public class PageResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "list";

    public static final String PAGE_SIZE = "pageSize";

    public static final String PAGE_NUM = "pageNum";

    /** 总数 */
    public static final String Total = "total";



    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public PageResult()
    {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public PageResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public PageResult(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put("data", data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static PageResult success()
    {
        return PageResult.success("操作成功");
    }


    public static PageResult success(Object data)
    {
        return PageResult.success("操作成功", data);
    }


    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static PageResult success(Object data,Integer pageSize,Integer pageNum,Integer total)
    {
        PageResult r = new PageResult();
        r.put(Total,total);
        r.put(PAGE_SIZE,pageSize);
        r.put(PAGE_NUM,pageNum);
        r.put(DATA_TAG,data);
        PageResult result = PageResult.success("操作成功",r);
        return result;
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static PageResult success(String msg)
    {
        return PageResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static PageResult success(String msg, Object data)
    {
        return new PageResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static PageResult error()
    {
        return PageResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static PageResult error(String msg)
    {
        return PageResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static PageResult error(String msg, Object data)
    {
        return new PageResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static PageResult error(int code, String msg)
    {
        return new PageResult(code, msg, null);
    }
}
