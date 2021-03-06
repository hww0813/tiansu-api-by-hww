package com.yuanqing.framework.web.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanqing.common.constant.HttpStatus;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.sql.SqlUtil;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.PageDomain;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.framework.web.page.TableSupport;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected PageDomain startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
        return pageDomain;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {


        TableDataInfo rspData = new TableDataInfo();

        if(CollectionUtils.isEmpty(list)){
            rspData.setCode(HttpStatus.SUCCESS);
            rspData.setList(null);
            rspData.setTotal(0);
            return rspData;
        }else{
            rspData.setCode(HttpStatus.SUCCESS);
            rspData.setList(list);
            rspData.setTotal(new PageInfo(list).getTotal());
            return rspData;
        }
    }

    /**
     * 响应请求分页数据（在startPage分页之后，又进行了数据处理，此时集合中的总数不正确，需原始list提取总数）
     * @param list 在分页之后处理的数据，最终数据集
     * @param originList 原始list 分页时的数据集，用于统计数据总数
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list, List<?> originList)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setList(list);
        rspData.setTotal(new PageInfo(originList).getTotal());
        return rspData;
    }

    protected TableDataInfo getDataTable(List<?> list, int total)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setList(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
