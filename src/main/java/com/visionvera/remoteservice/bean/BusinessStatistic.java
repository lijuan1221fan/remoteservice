package com.visionvera.remoteservice.bean;

/**
 * ClassName: BusinessStatistic
 *
 * @author quboka
 * @Description: 业务统计
 * @date 2018年3月31日
 */
public class BusinessStatistic {

  private String type;
  private String name;
  private Integer count;
  private Integer ready;
  private Integer start;
  private Integer end;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getReady() {
    return ready;
  }

  public void setReady(Integer ready) {
    this.ready = ready;
  }

  public Integer getStart() {
    return start;
  }

  public void setStart(Integer start) {
    this.start = start;
  }

  public Integer getEnd() {
    return end;
  }

  public void setEnd(Integer end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "BusinessStatistic [type=" + type + ", name=" + name
        + ", count=" + count + ", ready=" + ready + ", start=" + start
        + ", end=" + end + "]";
  }

}
