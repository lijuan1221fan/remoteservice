package com.visionvera.remoteservice.bean;

/**
 * Created by jlm on 2019-07-10 10:29
 */
public class StatistBasicsForCount {

    private Integer name;
    private Integer value;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public StatistBasicsForCount(Integer name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public StatistBasicsForCount() {
    }

    @Override
    public String toString() {
        return "StatistBasicsForCount{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
