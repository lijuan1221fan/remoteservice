package com.visionvera.remoteservice.pojo;/**
 * 功能描述:
 *
 * @ClassName: PrintVo
 * @Author: ljfan
 * @Date: 2019-01-24 21:35
 * @Version: V1.0
 */

import com.visionvera.common.validator.group.CheckGroup;
import javax.validation.constraints.NotNull;

/**
 * @ClassNamePrintVo
 *description 打印对象
 * @author ljfan
 * @date 2019/01/24
 *version
 */
public class PrintVo extends BaseVo {

  @NotNull(groups = {CheckGroup.class}, message = "id不能为空")
  private String ids;
  @NotNull(groups = {CheckGroup.class}, message = "业务id不能为空")
  private Integer businessId;

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }
}
