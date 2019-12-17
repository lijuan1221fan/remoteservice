package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by ljfan on 2018/11/2.
 */
public interface SysDeptService {

  /**
   * 添加部门
   *
   * @param sysDeptBean
   * @return
   * @
   */
  Map<String, Object> addSysDept(SysDeptBean sysDeptBean, MultipartFile stampFile);

  /**
   * 修改部门
   *
   * @param sysDeptBean
   * @return
   */
  Map<String, Object> updateSysDept(SysDeptBean sysDeptBean, MultipartFile stampFile);

  /**
   * 删除部门
   *
   * @param ids
   * @return
   */
  Map<String, Object> deleteSysDept(Integer[] ids);

  /**
   * 查询部门
   *
   * @param paramsMap
   * @return
   * @
   */
  Map<String, Object> getSysDeptList(SysDeptVo sysDeptVo);
  /**
   * 查询部门
   *
   * @param paramsMap
   * @return map
   */
  Map<String, Object> getSysDeptById(Integer deptId);


  /**
   * 查询部门  无分页
   *
   * @param id
   * @return
   * @
   */
  Map<String, Object> getSysDeptListNoPage(Integer id);

  /**
   * @param paramsMap
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:37
   */
  Map<String, Object> updateState(SysDeptBean sysDeptBean);

  /**
  　　* @Description: 查询部门，查询非删除状态，供系统设置页面使用
  　　* @author: xueshiqi
  　　* @date: 2018/12/4
  　　*/
  Map<String, Object> getSysDeptListNotInDelete();

  /**
   * 获取部门章路径
   * @param id
   * @return
   */
  Map<String, Object> getDeptStamp(Integer id);

  /**
   * 根据业务id获取部门章
   * @param businessId
   * @return
   */
  Map<String, Object> getDeptStampByBusinessId(Integer businessId);

  /**
   * 根据部门名称查询部门是否存在
   *
   * @param deptName
   * @return
   */
  Map<String, Object> getDeptByDeptName(String deptName);

  /**
   * 受理端业务变更时的部门列表
   * @return
   */
  Map<String, Object> deptListForChangeBusiness();

}
