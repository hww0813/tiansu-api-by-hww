package com.yuanqing.project.system.domain;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 告警规则对象 macs_event_rule
 *
 * @author gusheng
 * @date 2020-11-12
 */
public class MacsEventRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 规则名 */
    @Excel(name = "规则名")
    private String name;

    /** 关键字 */
    @Excel(name = "关键字")
    private String keyword;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 周几报警 */
    @Excel(name = "周几报警")
    private String effectWeek;

    /** 报警等级 */
    @Excel(name = "报警等级")
    private String eventLevel;

    /** 规则优先等级 */
    @Excel(name = "规则优先等级")
    private Long priority;

    /** 1：短信报警 */
    @Excel(name = "1：短信报警")
    private Long smsAlarm;

    /** 1：邮件报警 */
    @Excel(name = "1：邮件报警")
    private Long emailAlarm;

    /** 短信报警用户ids */
    @Excel(name = "短信报警用户ids")
    private String smsUserids;

    /** 邮件报警用户ids */
    @Excel(name = "邮件报警用户ids")
    private String emailUserids;

    /** 操作行为 */
    @Excel(name = "操作行为")
    private String actions;

    /** 选择的摄像头的id列表，以逗号隔开 */
    @Excel(name = "选择的摄像头的id列表，以逗号隔开")
    private String deviceIds;

    /** 事件分类：0-普通事件；1-操作事件；2-敏感事件；3-异常事件 */
    @Excel(name = "事件分类：0-普通事件；1-操作事件；2-敏感事件；3-异常事件")
    private Long eventCategory;

    /** 规则是否生效：0否；1是 */
    @Excel(name = "规则是否生效：0否；1是")
    private Integer onWork;

    /** sms */
    @Excel(name = "sms")
    private Integer sms;

    /** 是否选择ip范围 */
    @Excel(name = "是否选择ip范围")
    private Long ipRangeSelect;

    /** 指定ip范围 */
    @Excel(name = "指定ip范围")
    private String pointIpRange;

    /** 例外时间范围是否生效 */
    @Excel(name = "例外时间范围是否生效")
    private Long exceptIpOption;

    /** 例外时间范围 */
    @Excel(name = "例外时间范围")
    private String exceptIpRange;

    /** 规则是否生效：0否；1是 */
    @Excel(name = "规则是否生效：0否；1是")
    private Long ruleOrder;

    /** 时间范围是否生效 */
    @Excel(name = "时间范围是否生效")
    private Long timeRangeSelect;

    /** 指定开始日 */
    @Excel(name = "指定开始日")
    private Long pointSday;

    /** 指定结束日 */
    @Excel(name = "指定结束日")
    private Long pointEday;

    /** 指定开始月 */
    @Excel(name = "指定开始月")
    private String pointStime;

    /** 指定结束月 */
    @Excel(name = "指定结束月")
    private String pointEtime;

    /** 例外日期范围是否生效 */
    @Excel(name = "例外日期范围是否生效")
    private Long exceptTimeOption;

    /** 例外开始日 */
    @Excel(name = "例外开始日")
    private Long exceptSday;

    /** 例外结束日 */
    @Excel(name = "例外结束日")
    private Long exceptEday;

    /** 例外开始月 */
    @Excel(name = "例外开始月")
    private String exceptStime;

    /** 例外结束月 */
    @Excel(name = "例外结束月")
    private String exceptEtime;

    /** 关键字reg */
    @Excel(name = "关键字reg")
    private String keywordReg;

    /** 源ip范围 */
    @Excel(name = "源ip范围")
    private String srcIprange;

    /** 用户名 */
    @Excel(name = "用户名")
    private String usernames;

    /** 设备描述 */
    @Excel(name = "设备描述")
    private String deviceDescription;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 设备ip范围 */
    @Excel(name = "设备ip范围")
    private String deviceIprange;

    /** 目的ip范围 */
    @Excel(name = "目的ip范围")
    private String dstIpRange;

    /** 设备名字 */
    @Excel(name = "设备名字")
    private String deviceName;

    /** 设备编码 */
    @Excel(name = "设备编码")
    private String deviceCode;


    public String getDstIpRange() {
        return dstIpRange;
    }

    public void setDstIpRange(String dstIpRange) {
        this.dstIpRange = dstIpRange;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getKeyword()
    {
        return keyword;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setEffectWeek(String effectWeek)
    {
        this.effectWeek = effectWeek;
    }

    public String getEffectWeek()
    {
        return effectWeek;
    }
    public void setEventLevel(String eventLevel)
    {
        this.eventLevel = eventLevel;
    }

    public String getEventLevel()
    {
        return eventLevel;
    }
    public void setPriority(Long priority)
    {
        this.priority = priority;
    }

    public Long getPriority()
    {
        return priority;
    }
    public void setSmsAlarm(Long smsAlarm)
    {
        this.smsAlarm = smsAlarm;
    }

    public Long getSmsAlarm()
    {
        return smsAlarm;
    }
    public void setEmailAlarm(Long emailAlarm)
    {
        this.emailAlarm = emailAlarm;
    }

    public Long getEmailAlarm()
    {
        return emailAlarm;
    }
    public void setSmsUserids(String smsUserids)
    {
        this.smsUserids = smsUserids;
    }

    public String getSmsUserids()
    {
        return smsUserids;
    }
    public void setEmailUserids(String emailUserids)
    {
        this.emailUserids = emailUserids;
    }

    public String getEmailUserids()
    {
        return emailUserids;
    }
    public void setActions(String actions)
    {
        this.actions = actions;
    }

    public String getActions()
    {
        return actions;
    }
    public void setDeviceIds(String deviceIds)
    {
        this.deviceIds = deviceIds;
    }

    public String getDeviceIds()
    {
        return deviceIds;
    }
    public void setEventCategory(Long eventCategory)
    {
        this.eventCategory = eventCategory;
    }

    public Long getEventCategory()
    {
        return eventCategory;
    }
    public void setOnWork(Integer onWork)
    {
        this.onWork = onWork;
    }

    public Integer getOnWork()
    {
        return onWork;
    }
    public void setSms(Integer sms)
    {
        this.sms = sms;
    }

    public Integer getSms()
    {
        return sms;
    }
    public void setIpRangeSelect(Long ipRangeSelect)
    {
        this.ipRangeSelect = ipRangeSelect;
    }

    public Long getIpRangeSelect()
    {
        return ipRangeSelect;
    }
    public void setPointIpRange(String pointIpRange)
    {
        this.pointIpRange = pointIpRange;
    }

    public String getPointIpRange()
    {
        return pointIpRange;
    }
    public void setExceptIpOption(Long exceptIpOption)
    {
        this.exceptIpOption = exceptIpOption;
    }

    public Long getExceptIpOption()
    {
        return exceptIpOption;
    }
    public void setExceptIpRange(String exceptIpRange)
    {
        this.exceptIpRange = exceptIpRange;
    }

    public String getExceptIpRange()
    {
        return exceptIpRange;
    }
    public void setRuleOrder(Long ruleOrder)
    {
        this.ruleOrder = ruleOrder;
    }

    public Long getRuleOrder()
    {
        return ruleOrder;
    }
    public void setTimeRangeSelect(Long timeRangeSelect)
    {
        this.timeRangeSelect = timeRangeSelect;
    }

    public Long getTimeRangeSelect()
    {
        return timeRangeSelect;
    }
    public void setPointSday(Long pointSday)
    {
        this.pointSday = pointSday;
    }

    public Long getPointSday()
    {
        return pointSday;
    }
    public void setPointEday(Long pointEday)
    {
        this.pointEday = pointEday;
    }

    public Long getPointEday()
    {
        return pointEday;
    }
    public void setPointStime(String pointStime)
    {
        this.pointStime = pointStime;
    }

    public String getPointStime()
    {
        return pointStime;
    }
    public void setPointEtime(String pointEtime)
    {
        this.pointEtime = pointEtime;
    }

    public String getPointEtime()
    {
        return pointEtime;
    }
    public void setExceptTimeOption(Long exceptTimeOption)
    {
        this.exceptTimeOption = exceptTimeOption;
    }

    public Long getExceptTimeOption()
    {
        return exceptTimeOption;
    }
    public void setExceptSday(Long exceptSday)
    {
        this.exceptSday = exceptSday;
    }

    public Long getExceptSday()
    {
        return exceptSday;
    }
    public void setExceptEday(Long exceptEday)
    {
        this.exceptEday = exceptEday;
    }

    public Long getExceptEday()
    {
        return exceptEday;
    }
    public void setExceptStime(String exceptStime)
    {
        this.exceptStime = exceptStime;
    }

    public String getExceptStime()
    {
        return exceptStime;
    }
    public void setExceptEtime(String exceptEtime)
    {
        this.exceptEtime = exceptEtime;
    }

    public String getExceptEtime()
    {
        return exceptEtime;
    }
    public void setKeywordReg(String keywordReg)
    {
        this.keywordReg = keywordReg;
    }

    public String getKeywordReg()
    {
        return keywordReg;
    }
    public void setSrcIprange(String srcIprange)
    {
        this.srcIprange = srcIprange;
    }

    public String getSrcIprange()
    {
        return srcIprange;
    }
    public void setUsernames(String usernames)
    {
        this.usernames = usernames;
    }

    public String getUsernames()
    {
        return usernames;
    }
    public void setDeviceDescription(String deviceDescription)
    {
        this.deviceDescription = deviceDescription;
    }

    public String getDeviceDescription()
    {
        return deviceDescription;
    }
    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType()
    {
        return deviceType;
    }
    public void setDeviceIprange(String deviceIprange)
    {
        this.deviceIprange = deviceIprange;
    }

    public String getDeviceIprange()
    {
        return deviceIprange;
    }
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("keyword", getKeyword())
            .append("description", getDescription())
            .append("effectWeek", getEffectWeek())
            .append("eventLevel", getEventLevel())
            .append("priority", getPriority())
            .append("smsAlarm", getSmsAlarm())
            .append("emailAlarm", getEmailAlarm())
            .append("smsUserids", getSmsUserids())
            .append("emailUserids", getEmailUserids())
            .append("actions", getActions())
            .append("deviceIds", getDeviceIds())
            .append("eventCategory", getEventCategory())
            .append("onWork", getOnWork())
            .append("sms", getSms())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("ipRangeSelect", getIpRangeSelect())
            .append("pointIpRange", getPointIpRange())
            .append("exceptIpOption", getExceptIpOption())
            .append("exceptIpRange", getExceptIpRange())
            .append("ruleOrder", getRuleOrder())
            .append("timeRangeSelect", getTimeRangeSelect())
            .append("pointSday", getPointSday())
            .append("pointEday", getPointEday())
            .append("pointStime", getPointStime())
            .append("pointEtime", getPointEtime())
            .append("exceptTimeOption", getExceptTimeOption())
            .append("exceptSday", getExceptSday())
            .append("exceptEday", getExceptEday())
            .append("exceptStime", getExceptStime())
            .append("exceptEtime", getExceptEtime())
            .append("keywordReg", getKeywordReg())
            .append("srcIprange", getSrcIprange())
            .append("usernames", getUsernames())
            .append("deviceDescription", getDeviceDescription())
            .append("deviceType", getDeviceType())
            .append("deviceIprange", getDeviceIprange())
            .append("dstIpRange", getDstIpRange())
            .append("deviceName", getDeviceName())
            .append("deviceCode", getDeviceCode())
            .toString();
    }
}
