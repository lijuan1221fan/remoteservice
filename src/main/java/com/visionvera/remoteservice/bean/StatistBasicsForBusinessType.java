package com.visionvera.remoteservice.bean;

import java.util.List;

/**
 * ClassName: StatistBasics
 *
 * @author quboka
 * @Description: 统计的基础类
 * @date 2018年4月9日
 */
public class StatistBasicsForBusinessType {

  /**
   * @Fields name : 统计项的名称
   */
  private String name;
  /**
   * @Fields value : 对应的值
   */
  private Integer value;
  /**
   * 用于业务类型关联业务详情
   */
  private Integer id;
  /**
   * 用于遍历处理业务类型关联对业务类型列表
   */
  private List<StatistBasics> businessStatisStatus;

  public StatistBasicsForBusinessType() {
    super();
  }

  public StatistBasicsForBusinessType(String name, Integer value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<StatistBasics> getBusinessStatisStatus() {
    return businessStatisStatus;
  }

  public void setBusinessStatisStatus(List<StatistBasics> businessStatisStatus) {
    this.businessStatisStatus = businessStatisStatus;
  }

  @Override
  public String toString() {
    return "StatistBasics [name=" + name + ", value=" + value + "]";
  }


}
