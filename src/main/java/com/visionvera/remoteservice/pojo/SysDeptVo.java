package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author ljfan
 * @ClassNameSysDeptVo 部门基于分页参数的实体
 * @date 2018/12/21 version
 */
public class SysDeptVo extends BaseVo {

  @NotEmpty(groups = {UpdateGroup.class}, message = "部门主键不能为空")
  private int id;

  private Integer[] ids;
  /**
   * 部门名称
   */
  @Length(min = 1, max = 16, groups = {AddGroup.class,
      UpdateGroup.class}, message = "length长度在[1,16]之间")
  @NotEmpty(groups = {AddGroup.class, UpdateGroup.class}, message = "部门名称不能为空")
  private String deptName;
  /**
   * 部门状态  -2 删除  -1：禁用  0：启用
   */
  @NotNull(groups = {AddGroup.class, UpdateGroup.class}, message = "部门状态不能为空")
  private Integer state;
  /**
   * 部门章路径
   */
  private String stampUrl;

  /**
   * 类型
   * @return
   */
  private Integer type;

  public String getStampUrl() {
    return stampUrl;
  }

  public void setStampUrl(String stampUrl) {
    this.stampUrl = stampUrl;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer[] getIds() {
    return ids;
  }

  public void setIds(Integer[] ids) {
    this.ids = ids;
  }

}
