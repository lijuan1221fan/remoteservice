package com.visionvera.remoteservice.bean;
/**
 * @ClassNameUserSortVo
 *description ridis中 用户进入队列并加入时间
 * @author ljfan
 * @date 2019/07/19
 *version
 */
public class UserSortVo {
  private Long sortTime;
  private Integer userId;

  public Long getSortTime() {
    return sortTime;
  }

  public void setSortTime(Long sortTime) {
    this.sortTime = sortTime;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
