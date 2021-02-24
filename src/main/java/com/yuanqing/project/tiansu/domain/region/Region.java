package com.yuanqing.project.tiansu.domain.region;

import java.io.Serializable;
import java.util.List;

/**
 * 行政区域编码
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/17
 */
public class Region implements Serializable {

    private Long id;
    private String name; //名称
    private Long parentId; //上级行政区域
    private String shortName; //简称
    private int level; //层级
    private String mergerName; //全称
    private String pinYin; //拼音
    private List<Region> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public List<Region> getChildren() {
        return children;
    }

    public void setChildren(List<Region> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", shortName='" + shortName + '\'' +
                ", level=" + level +
                ", mergerName='" + mergerName + '\'' +
                ", pinYin='" + pinYin + '\'' +
                ", children=" + children +
                '}';
    }
}