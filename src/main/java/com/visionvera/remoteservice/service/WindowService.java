package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.WindowBean;

import java.util.Map;

/**
 * @author jlm
 * @ClassName: WindowService
 * @Description: 窗口Service接口
 * @date 2018/10/31
 */
public interface WindowService {

    /**
     * 添加窗口
     *
     * @param windowBean
     * @return
     * @
     */
    Map<String, Object> addWindow(WindowBean windowBean);

    /**
     * 删除窗口
     *
     * @param idArray
     * @return
     * @
     */
    Map<String, Object> deleteWindow(String[] idArray);

    /**
     * 修改窗口
     *
     * @param windowBean
     * @return
     * @
     */
    Map<String, Object> updateWindow(WindowBean windowBean);

    /**
     * 禁/启用窗口
     *
     * @param id
     * @param status
     * @return
     * @
     */
    Map<String, Object> lockWindow(Integer id, Integer status);

    /**
     * 查询窗口
     *
     * @param map
     * @return
     * @
     */
    Map<String, Object> getWindow(Map<String, Object> map);

    /**
     * 根据id查询窗口
     *
     * @param id
     * @return
     * @
     */
    WindowBean getWindowById(Integer id);

    /**
     * 下拉列表查询窗口
     *
     * @param paramMap
     * @return
     * @
     */
    Map<String, Object> getListWindow(Map<String, Object> paramMap);

    Map<String, Object> setWindowFree(Integer id);

    /**
     * 将用户和窗口进行关联
     *
     * @param id
     * @return
     */
    Map<String, Object> addWindowToUser(Integer id);

}
