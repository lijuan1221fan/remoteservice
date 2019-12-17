package com.visionvera.remoteservice.pojo;

/**
 * Created by jlm on 2019-08-09 15:23
 */
public class StatisticalDataVo {

    private String serviceKey;
    private Integer deptId;

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public StatisticalDataVo() {
    }

    public StatisticalDataVo(String serviceKey, Integer deptId) {
        this.serviceKey = serviceKey;
        this.deptId = deptId;
    }
}
