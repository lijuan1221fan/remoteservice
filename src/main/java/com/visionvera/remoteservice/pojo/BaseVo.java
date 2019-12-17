package com.visionvera.remoteservice.pojo;

import com.visionvera.common.validator.group.QueryGroup;
import javax.validation.constraints.NotNull;

/**
 * @author jlm
 * @ClassName:
 * @Description: 基础pojo参数类
 * @date 2018/12/17
 */
public class BaseVo {

  @NotNull(groups = QueryGroup.class, message = "页码不能为空")
  public Integer pageNum = 1;
  @NotNull(groups = QueryGroup.class, message = "每页记录数不能为空")
  public Integer pageSize = 20;

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }
}
