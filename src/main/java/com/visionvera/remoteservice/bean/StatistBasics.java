package com.visionvera.remoteservice.bean;

/**
 * ClassName: StatistBasics
 *
 * @author quboka
 * @Description: 统计的基础类
 * @date 2018年4月9日
 */
public class StatistBasics {

  /**
   * @Fields name : 统计项的名称
   */
  private String name;
  /**
   * @Fields value : 对应的值
   */
  private Integer value;

  public StatistBasics() {
    super();
  }

  public StatistBasics(String name, Integer value) {
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

  @Override
  public String toString() {
    return "StatistBasics [name=" + name + ", value=" + value + "]";
  }


}
