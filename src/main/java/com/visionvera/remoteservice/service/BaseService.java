package com.visionvera.remoteservice.service;

import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * Service 接口基类 ClassName: BaseService <br/> Function: TODO ADD FUNCTION. <br/> date: 2018年5月25日
 * <br/>
 *
 * @author zhanghui
 * @version @param <T>
 * @since JDK 1.7
 */
public interface BaseService<T> {

  /**
   * 获取单条数据
   *
   * @param id
   * @return
   */
  T get(String id);


  /**
   * 获取单条数据
   *
   * @param entity
   * @return
   */
  T get(T entity);

  /**
   * 查询数据列表
   *
   * @param entity
   * @return
   */
  List<T> queryList(T entity);

  /**
   * 分页查询数据列表
   *
   * @param entity
   * @return
   */
  PageInfo<T> queryListPage(T entity, Integer pageNum, Integer pageSize);


  /**
   * 保存数据（插入或更新）
   *
   * @param entity
   */
  int save(T entity);

  /**
   * 更新数据
   *
   * @param entity
   */
  int update(T entity);

  /**
   * 删除数据
   *
   * @param entity
   */
  int delete(T entity);

  /**
   * 查询总记录数
   *
   * @param entity
   */
  int queryCount(T entity);
}
