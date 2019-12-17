package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @auther ljfan
 */
public interface SysDeptBeanDao {

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int insert(SysDeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int insertSelective(SysDeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  SysDeptBean selectByPrimaryKey(Integer id);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int updateByPrimaryKeySelective(SysDeptBean record);

  /**
   * @mbg.generated Thu Aug 16 14:42:07 CST 2018
   */
  int updateByPrimaryKey(SysDeptBean record);

  /**
   * 校验名称
   *
   * @param id
   * @param deptName
   * @return
   */
  int checkTheName(@Param("id") Integer id, @Param("deptName") String deptName);

  /**
   * 删除
   *
   * @param ids
   * @return
   */
  int deleteDeptByIds(Integer[] ids);

  /**
   * 查询接口
   *
   * @param paramsMap
   * @return
   */
  List<SysDeptBean> getDeptList(SysDeptVo sysDeptVo);

  /**
   * 查询接口
   *
   * @param id
   * @return
   */
  List<SysDeptBean> getSysDeptListNoPage(@Param("id") Integer id);

  /**
   * @param deptId
   * @return java.util.List<java.lang.String>
   * @description: 获取标题
   * @author quboka
   * @date 2018/8/22 14:42
   */
  List<String> getDeptTitle(@Param("deptId") Integer deptId);

  /**
   * @param deptTitle, deptId
   * @return int
   * @description: 修改标题
   * @author quboka
   * @date 2018/8/22 16:01
   */
  int updateServiceTitle(@Param("deptTitle") String deptTitle,
      @Param("deptId") Integer deptId);

  /*
   *查询当前最大叫号前置值
   *
   * @author ljfan
   * @date 2018/10/30
   */
  String selectMaxNumberPrefix();

  /**
   * @param paramsMap
   * @return int
   * @description: 修改状态
   * @author ljfan
   * @date 2018/8/30 17:40
   */
  int updateState(SysDeptBean sysDeptBean);

  /**
  　　* @Description: 查询部门，查询非删除状态，供系统设置页面使用
  　　* @author: xueshiqi
  　　* @date: 2018/12/4
  　　*/
  List<SysDeptBean> getSysDeptListNotInDelete();

  /**
   * @return 添加部门最多只能建三个校验
   */
  Integer checkLimitNumber();

  /**
  　　* @Description: 根据主键查询详情
  　　* @author: xueshiqi
  　　* @date: 2018/12/28
  　　*/
  SysDeptBean getDeptInfo(Integer deptId);
  /**
  　　* @Description: 根据主键查询详情
  　　* @author: xueshiqi
  　　* @date: 2018/12/28
  　　*/
  SysDeptBean getDeptByDeptId(@Param(value = "deptId") Integer deptId);

  /**
   * 查询部门章
   * @param id
   * @return
   */
  SysDeptBean getDeptStamp(Integer id);

  /**
   * 根据部门名称查询部门
   *
   * @param deptName
   * @return
   */
  SysDeptBean getDeptByDeptName(String deptName);

  /**
   * 受理端业务变更时的部门列表     
   * @return
   */
  List<SysDeptBean> deptListForChangeBusiness(SysDeptBean sysDeptBean);
  List<SysDeptBean> specialDeptList(@Param(value = "type") Integer type);
}
