package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.DropDownGroup;
import com.visionvera.common.validator.group.LockGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2018/12/17
 */
public class WindowVo extends BaseVo {

  @Length(min = 1, max = 16, message = "窗口名应为1到16字符", groups = {AddGroup.class, UpdateGroup.class})
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class}, message = "窗口名不能为空")
  private String windowName;
  @NotBlank(groups = {AddGroup.class, UpdateGroup.class,
      DropDownGroup.class}, message = "中心Key不能为空")
  private String serviceKey;
  @NotNull(groups = {AddGroup.class, UpdateGroup.class, DropDownGroup.class}, message = "部门id不能为空")
  private Integer deptId;
  @NotNull(groups = {AddGroup.class, UpdateGroup.class, LockGroup.class}, message = "状态不能为空")
  private Integer state;
  @NotNull(groups = {UpdateGroup.class, LockGroup.class}, message = "窗口id不能为空")
  private Integer id;

  public String getWindowName() {
    return windowName;
  }

  public void setWindowName(String windowName) {
    this.windowName = windowName;
  }

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

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
