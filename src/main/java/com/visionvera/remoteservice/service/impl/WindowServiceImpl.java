package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.remoteservice.bean.*;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.*;
import com.visionvera.remoteservice.service.WindowService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author jlm
 * @ClassName: WindowServiceImpl
 * @Description: 窗口service实现类
 * @date 2018/10/31
 */
@Service
public class WindowServiceImpl implements WindowService {

    private static Logger logger = LoggerFactory.getLogger(WindowServiceImpl.class);

    @Resource
    private WindowDao windowDao;
    @Resource
    private ServiceCenterDao serviceCenterDao;
    @Resource
    private SysDeptBeanDao sysDeptBeanDao;
    @Resource
    private BusinessInfoDao businessInfoDao;
    @Resource
    private VcDevDao vcDevDao;

    @Override
    public Map<String, Object> addWindow(WindowBean windowBean) {
        List<WindowBean> windowList = windowDao.getWindowByServiceKeyAndDeptId(windowBean);
        if (windowList.size() > 0) {
            return ResultUtils.error("所选中心的部门下已有同名窗口");
        }
        //校验中心是否可用
        ServiceCenterBean serviceCenter = serviceCenterDao
                .getServiceCenterByServiceKey(windowBean.getServiceKey());
        if (StateEnum.Invalid.getValue().equals(serviceCenter.getState())) {
            logger.info("添加失败，中心不存在，请刷新后重试");
            return ResultUtils.error("添加失败，中心不存在，请刷新后重试");
        }
        //校验部门是否可用
        SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(windowBean.getDeptId());
        if (windowBean.getDeptId() != 0) {
            if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
                logger.info("添加失败，部门不存在");
                return ResultUtils.error("添加失败，部门不存在，请刷新后重试");
            }
        }
        int i = windowDao.addWindow(windowBean);
        if (i > CommonConstant.zero) {

            logger.info("添加窗口成功");
            return ResultUtils.ok("添加成功");
        }
        logger.info("添加窗口失败");
        return ResultUtils.error("添加失败");
    }

    @Override
    public Map<String, Object> deleteWindow(String[] idArray) {
        int i = windowDao.deleteWindow(idArray);
        if (i > CommonConstant.zero) {
            logger.info("删除窗口成功");
            return ResultUtils.ok("删除成功");
        }
        logger.info("删除窗口失败");
        return ResultUtils.error("删除失败");
    }

    @Override
    public Map<String, Object> updateWindow(WindowBean windowBean) {
        WindowBean windowById = windowDao.getWindowById(windowBean.getId());
        if (windowById == null) {
            return ResultUtils.error("窗口不存在，请刷新页面");
        }
        if (windowById.getIsUse().equals(CommonConstant.ISUSE) || windowById.getIsUse().equals(WindowUseStateEnum.Wait.getValue())) {
            return ResultUtils.error("禁止修改正在服务的窗口");
        }
        List<WindowBean> windowList = windowDao.getWindowByServiceKeyAndDeptId(windowBean);
        if (windowList.size() > 0 && windowList.get(CommonConstant.zero).getId() != windowBean
                .getId()) {
            return ResultUtils.error("所选中心的部门下已有同名窗口");
        }
        //校验中心是否可用
        ServiceCenterBean serviceCenter = serviceCenterDao
                .getServiceCenterByServiceKey(windowBean.getServiceKey());
        if (StateEnum.Invalid.getValue().equals(serviceCenter.getState())) {
            logger.info("修改失败，中心不存在，请刷新后重试");
            return ResultUtils.error("修改失败，中心不存在，请刷新后重试");
        }
        //校验部门是否可用
        if (ShiroUtils.getUserEntity().getDeptId() != 0) {
            SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(windowBean.getDeptId());
            if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
                logger.info("添加失败，部门不存在");
                return ResultUtils.error("添加失败，部门不存在，请刷新后重试");
            }
        }
        //校验该窗口是否存在绑定的终端，若存在绑定终端则不可修改窗口的serviceKey以及dept
        VcDevBean vcdev = vcDevDao.getDeviceByWindowId(windowBean.getId());
        if (vcdev != null && (!windowBean.getServiceKey().equals(windowById.getServiceKey()) || !windowBean.getDeptId().equals(windowById.getDeptId()))) {
            return ResultUtils.error("修改失败，请解绑终端后，再次进行修改");
        }

        int i = windowDao.updateWindow(windowBean);
        if (i > CommonConstant.zero) {
            logger.info("修改窗口成功");
            return ResultUtils.ok("修改成功");
        }
        logger.info("修改窗口失败");
        return ResultUtils.error("修改失败");
    }

    @Override
    public Map<String, Object> lockWindow(Integer id, Integer status) {
        WindowBean windowById = windowDao.getWindowById(id);
        if (windowById == null) {
            return ResultUtils.error("窗口不存在，请刷新页面");
        }
        if (windowById.getIsUse().equals(CommonConstant.ISUSE)) {
            return ResultUtils.error("拒绝禁用正在服务的窗口");
        }
        int i = windowDao.lockWindow(id, status);
        if (i > CommonConstant.zero) {
            logger.info("设置成功");
            return ResultUtils.ok("设置成功");
        }
        logger.info("设置失败");
        return ResultUtils.error("设置失败");
    }

    @Override
    public Map<String, Object> getWindow(Map<String, Object> paramMap) {
        Integer pageNum = (Integer) paramMap.get("pageNum");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        SysUserBean userBean = ShiroUtils.getUserEntity();
        List<String> serviceKeys = null;
        if (SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())) {
            List<String> serviceKeyList = serviceCenterDao.getServiceListByParentKey(userBean.getServiceKey());
            serviceKeys = new ArrayList<>();
            serviceKeys.addAll(serviceKeyList);
            //统筹也有自己的窗口
            serviceKeys.add(userBean.getServiceKey());
            paramMap.put("serviceKeys", serviceKeys);
        } else if (SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
            serviceKeys = new ArrayList<>();
            serviceKeys.add(userBean.getServiceKey());
            paramMap.put("serviceKeys", serviceKeys);
        }
        paramMap.put("serviceKey", null);
        PageHelper.startPage(pageNum, pageSize);
        List<WindowBean> windowList = windowDao.getWindow(paramMap);
        PageInfo<WindowBean> pageInfo = new PageInfo<WindowBean>(windowList);
        logger.info("窗口分页查询成功");
        return ResultUtils.ok("窗口分页查询成功", pageInfo);
    }

    @Override
    public WindowBean getWindowById(Integer id) {
        WindowBean window = windowDao.getWindowById(id);
        return window;
    }

    @Override
    public Map<String, Object> getListWindow(Map<String, Object> paramMap) {
        List<WindowBean> listWindow = windowDao.getListWindow(paramMap);
        return ResultUtils.ok("窗口列表查询成功", listWindow);
    }

    @Override
    public Map<String, Object> setWindowFree(Integer id) {
        Integer i = windowDao.setWindowFree(id);
        if (i > CommonConstant.zero) {
            return ResultUtils.ok("置为空闲成功");
        }
        return ResultUtils.error("置为空闲失败");
    }

    @Override
    public Map<String, Object> addWindowToUser(Integer id) {
        return getResultMap(id);
    }

    private Map<String, Object> getResultMap(Integer id) {
        //首先判断前端传到后台的窗口是否被删除
        WindowBean windowBean = windowDao.getWindowById(id);
        if (windowBean == null) {
            logger.info("窗口已被删除，请重新选择");
            return ResultUtils.error("窗口已被删除，请重新选择");
        }
        //获取当前登录用户
        SysUserBean sysUserBean = ShiroUtils.getUserEntity();
        //判断当前用户是否有未完成业务
        BusinessInfo businessByUserId = businessInfoDao.getBusinessByUserId(sysUserBean.getUserId());
        if (businessByUserId != null) {
            //当前用户存在未完成业务 判断未完成业务窗口与当前窗口是否一致
            if (businessByUserId.getHandleWindowId().equals(id)) {
                //一致 发送该消息至前端 前端接收该消息并不提示
                logger.info("窗口与未完成业务窗口一致");
                return ResultUtils.ok("窗口与未完成业务窗口一致", windowBean);
            } else {
                //不一致 进行提示
                WindowBean windowById = windowDao.getWindowById(businessByUserId.getHandleWindowId());
                logger.info("窗口选择失败，您在" + windowById.getWindowName() + "窗口有未完结的业务，请前往办理");
                return ResultUtils.error("窗口选择失败，您在" + windowById.getWindowName() + "窗口有未完结的业务，请前往办理");
            }
        } else {
            //判断当前窗口是否与用户存在关联
            SysUserBean windowUser = windowDao.getWindowUserByWinowId(id);
            if (windowUser != null && !windowUser.getUserId().equals(sysUserBean.getUserId())) {
                //判断与窗口关联的用户是否存在未完成业务
                BusinessInfo businessInfo = businessInfoDao.getBusinessByUserId(windowUser.getUserId());
                if (businessInfo != null) {
                    logger.info("窗口选择失败，当前窗口有未完结的业务，请" + windowUser.getUserName() + "用户登录继续办理");
                    return ResultUtils.error("窗口选择失败，当前窗口有未完结的业务，请" + windowUser.getUserName() + "用户登录继续办理");
                } else {
                    logger.info(windowBean.getWindowName() + "窗口正在使用中，请联系管理员");
                    return ResultUtils.error(windowBean.getWindowName() + "窗口正在使用中，请联系管理员");
                }
            }
            //窗口与用户不存在关联关系 判断窗口的servicekey和deptId与当前登录用户是否匹配
            if (!windowBean.getServiceKey().equals(sysUserBean.getServiceKey()) || !windowBean.getDeptId().equals(sysUserBean.getDeptId())) {
                logger.info("窗口选择失败，您所在的受理点或部门与窗口不符，请联系管理员");
                return ResultUtils.error("窗口选择失败，您所在的受理点或部门与窗口不符，请联系管理员");
            }
        }
        //切换时 如用户存在关联关系 删除原关联
        WindowBean window = windowDao.getUserWindowByUserId(sysUserBean.getUserId());
        if (window != null) {
            logger.info("执行切换删除原关系");
            windowDao.deleteWindowAndUserRelation(null, sysUserBean.getUserId());
        }
        windowDao.insertWindowAndUserRelation(id, sysUserBean.getUserId());
        logger.info("窗口选择成功");
        return ResultUtils.ok("窗口选择成功", windowBean);
    }
}
