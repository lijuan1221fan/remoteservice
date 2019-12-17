package com.visionvera.remoteservice.bean;

/**
 * ClassName: DictionaryBean
 *
 * @author quboka
 * @Description: 通用字典表
 * @date 2018年4月10日
 */
public class DictionaryBean {

  private String key;
  private String value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "DictionaryBean [key=" + key + ", value=" + value + "]";
  }

}
