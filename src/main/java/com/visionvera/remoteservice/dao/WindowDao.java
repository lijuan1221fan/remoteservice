package com.visionvera.remoteservice.dao;

import com.visionvera.app.pojo.AppWindowParaVo;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.WindowBean;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author jlm
 * @ClassName: WindowDao
 * @Description: 窗口
 * @date 2018/10/31
 */
public interface WindowDao extends CrudDao<WindowBean> {

  /**
   * 添加窗口
   *
   * @param windowBean
   * @return
   */
  int addWindow(WindowBean windowBean);

  /**
   * 删除窗口
   *
   * @param idArray
   * @return
   */
  int deleteWindow(String[] idArray);

  /**
   * 修改窗口信息
   *
   * @param windowBean
   * @return
   */
  int updateWindow(WindowBean windowBean);

  /**
   * 禁/启用窗口
   *
   * @param id
   * @param status
   * @return
   */
  int lockWindow(Integer id, Integer status);

  /**
   * 查询窗口
   *
   * @param map
   * @return
   */
  List<WindowBean> getWindow(Map<String, Object> map);

  /**
   * 根据id查询窗口
   *
   * @param id
   * @return
   */
  WindowBean getWindowById(@Param(value = "id") Integer id);

  /**
   * 获取窗口下拉框
   *
   * @param map
   * @return
   */
  List<WindowBean> getListWindow(Map<String, Object> map);

  /**
   * 根据部门查找窗口
   *
   * @param deptIds
   * @return
   */
  List<WindowBean> getWindowByDeptIds(@Param(value = "ids") Integer[] ids);

  /**
   　　* @Description:根据serviceKeys查询窗口
   　　* @author: xueshiqi
   　　* @date: 2018/11/28 0028
   　　*/
  List<WindowBean> getWindowByServiceKeys(@Param("serviceKeys") List<String> serviceKeys);

  /**
   　　* @Description:根据审批中心serviceKeys以及部门Id查询窗口
   　　* @author: fanlj
   　　* @date: 2019/04/28 0028
   　　*/
  List<WindowBean> getWindowByServiceKeysAndDeptIds(AppWindowParaVo appWindowParaVo);

  /**
   * 同一中心同一部门下面窗口名不能重复，根据key和deptId,windowName进行查询
   *
   * @param windowBean
   * @return
   */
  List<WindowBean> getWindowByServiceKeyAndDeptId(WindowBean windowBean);

  /**
   * 将窗口置为空闲状态
   *
   * @param id
   * @return
   */
  Integer setWindowFree(@Param("id") Integer id);

  /**
   * 定期将窗口置为空闲状态
   */
  Integer setWindowFreeForAll();

  /**
   * 根据用户id查询窗口
   */
  WindowBean getUserWindowByUserId(@Param("userId") Integer userId);

  /**
   * 根据窗口id查询用户id
   */
  SysUserBean getWindowUserByWinowId(@Param("windowId") Integer windowId);

  /**
   * 插入用户和窗口关联关系
   */
  Integer insertWindowAndUserRelation(@Param("windowId") Integer windowId, @Param("userId") Integer userId);

  /**
   * 删除用户和窗口关联关系
   */
  Integer deleteWindowAndUserRelation(@Param("windowId") Integer windowId, @Param("userId") Integer userId);

  /**
   * 修改窗口状态
   */
  Integer updateWindowUseStatus(@Param("windowId") Integer windowId, @Param("state") Integer state);

  /**
   * 查询工作状态的窗口
   */
  List<WindowBean> getListWorkingWindows();

  /**
   * 将用户对应窗口置为等待状态
   */
  Integer updateWindowUseStatusByUserId(@Param("userId") Integer userId);
}
