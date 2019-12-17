package com.visionvera.remoteservice.bean;

import com.visionvera.common.enums.DataTypeEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * X86配置
 * 
 */
public class X86ConfigInformationBean implements Serializable {
	/***主键*/
	private Integer id;
	/***英文名*/
	private String columnNameEn;
	/***字段中文名*/
	private String columnNameCn;
	/***字段值*/
	private String columnValue;
	/***修改时间*/
	private Date updateTime;
	/***创建时间*/
	private Date createTime;
	/***状态 1:有效 0:无效 */
	private Integer state;
  /***状态  */
	private String describes;

	/**
	 * 数据类型  1：String  2：Integer   3：Float   4:Boolean
	 */
	private Integer dataType;

	/**
	 * 配置类型：1：x86配置文件  2：x86升级包
	 */
	private Integer configType;
  private  String regex;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getColumnNameEn() {
    return columnNameEn;
  }

  public void setColumnNameEn(String columnNameEn) {
    this.columnNameEn = columnNameEn;
  }

  public String getColumnNameCn() {
    return columnNameCn;
  }

  public void setColumnNameCn(String columnNameCn) {
    this.columnNameCn = columnNameCn;
  }

  public String getColumnValue() {
    return columnValue;
  }

  public void setColumnValue(String columnValue) {
    this.columnValue = columnValue;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getDescribes() {
    return describes;
  }

  public void setDescribes(String describes) {
    this.describes = describes;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public Integer getConfigType() {
    return configType;
  }

  public void setConfigType(Integer configType) {
    this.configType = configType;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public static HashMap<String, Object> convert(List<X86ConfigInformationBean> list, Integer type) {
		HashMap<String, Object> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(list) && type != null) {
			map.put("type", type);
			for (X86ConfigInformationBean x86ConfigInformationBean : list) {
				if (DataTypeEnum.String.getValue().equals(x86ConfigInformationBean.getDataType())) {
					map.put(x86ConfigInformationBean.getColumnNameEn(),
							x86ConfigInformationBean.getColumnValue());
				} else if (DataTypeEnum.Integer.getValue().equals(x86ConfigInformationBean.getDataType())) {
					map.put(x86ConfigInformationBean.getColumnNameEn(),
							Integer.parseInt(x86ConfigInformationBean.getColumnValue()));
				} else if (DataTypeEnum.Float.getValue().equals(x86ConfigInformationBean.getDataType())) {
					map.put(x86ConfigInformationBean.getColumnNameEn(),
							Float.parseFloat(x86ConfigInformationBean.getColumnValue()));
				} else if (DataTypeEnum.Boolean.getValue().equals(x86ConfigInformationBean.getDataType())) {
					map.put(x86ConfigInformationBean.getColumnNameEn(),
							Boolean.parseBoolean(x86ConfigInformationBean.getColumnValue()));
				}
			}
		}
		return map;
	}
  /**
   * 页面返回参数校验
   */
  public static String check(List<X86ConfigInformationBean> list){
    for (X86ConfigInformationBean bean:list) {
      String regex =bean.getRegex();
      if(!bean.getColumnValue().matches(regex)){
           return bean.columnNameCn;
      }
    }
    return "";
  }

}
