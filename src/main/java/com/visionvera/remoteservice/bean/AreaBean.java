package com.visionvera.remoteservice.bean;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.io.Serializable;
import java.sql.Date;

public class AreaBean implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 父级id
     */
    private String pid;
    /**
     * 地区名称
     */
    private String name;
    /**
     * 区域级别id
     */
    private Integer gradeId;
    /**
     * 详细地址
     */
    private String detail;
    /**
     * 更新时间
     */
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
