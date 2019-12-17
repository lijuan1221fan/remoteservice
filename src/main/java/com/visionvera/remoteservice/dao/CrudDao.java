package com.visionvera.remoteservice.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * DAO支持类实现
 *
 * @param <T>
 * @author zhanghui
 * @version 2018-05-25
 */
public interface CrudDao<T> {


  /**
   * 获取单条数据
   *
   * @param id
   * @return
   */
  T selectByPrimaryKey(Object id);

  /**
   * 获取单条数据
   *
   * @param entity
   * @return
   */
  T get(T entity);

  /**
   * 查询所有数据列表
   *
   * @param entity
   * @return
   */
  List<T> queryList(T entity);

  /**
   * 获得查询结果总数
   *
   * @param entity
   * @return
   */
  int queryCount(T entity);

  /**
   * 插入数据 全部字段
   *
   * @param entity
   * @return
   */
  int insert(T entity);

  /**
   * 插入数据 有值的字段
   */
  int insertSelective(T entity);

  /**
   * 根据主键更新数据实体中有值的字段
   *
   * @param entity
   * @return
   */
  int updateByPrimaryKeySelective(T entity);

  /**
   * 根据主键更新数据
   *
   * @param entity
   * @return
   */
  int updateByPrimaryKey(T entity);

  /**
   * 删除数据（一般为逻辑删除，更新del_flag字段为1）
   *
   * @param id
   * @return
   * @see public int delete(T entity)
   */
  int deleteByPrimaryKey(String id);

  /**
   * 删除数据 按条件删除
   *
   * @param entity
   * @return
   */
  int delete(T entity);


}