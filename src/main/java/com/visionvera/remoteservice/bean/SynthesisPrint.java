package com.visionvera.remoteservice.bean;

import java.util.List;
import javax.validation.constraints.*;

/**
 * @author xueshiqi
 * @Description: TODO
 * @date 2018/12/20 0020
 */
public class SynthesisPrint {

  @NotEmpty(message = "文件路径不能为空")
  private List<String> urls;

  @NotNull(message = "业务id不能为空")
  private Integer businessId;

  public List<String> getUrls() {
    return urls;
  }

  public void setUrls(List<String> urls) {
    this.urls = urls;
  }

  public Integer getBusinessId() {
    return businessId;
  }

  public void setBusinessId(Integer businessId) {
    this.businessId = businessId;
  }
}
