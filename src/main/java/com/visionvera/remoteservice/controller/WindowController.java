package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.enums.VcDevStateEnum;
import com.visionvera.common.enums.WindowUseStateEnum;
import com.visionvera.common.validator.group.AddGroup;
import com.visionvera.common.validator.group.DropDownGroup;
import com.visionvera.common.validator.group.LockGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.UpdateGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.VcDevBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.VcDevDao;
import com.visionvera.remoteservice.pojo.WindowVo;
import com.visionvera.remoteservice.service.WindowService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: WindowController
 *
 * @author jlm
 * @Description: 窗口
 * @date 2018年10月31日
 */
@RestController
@RequestMapping("/window")
public class WindowController {


    @Resource
    private WindowService windowService;
    @Resource
    private VcDevDao vcDevDao;

    @SysLogAnno("新建窗口")
    @RequiresPermissions("window:add")
    @RequestMapping(value = "/addWindow", method = RequestMethod.POST)
    public Map<String, Object> addWindow(@RequestBody WindowVo windowVo) {
        ValidateUtil.validate(windowVo, AddGroup.class);
        WindowBean windowBean = new WindowBean();
        BeanUtils.copyProperties(windowVo, windowBean);
        return windowService.addWindow(windowBean);
    }

    @SysLogAnno("删除窗口")
    @RequiresPermissions("window:delete")
    @RequestMapping(value = "/delWindow", method = RequestMethod.GET)
    public Map<String, Object> deleteWindow(@RequestParam(value = "ids") String ids) {
        ValidateUtil.validate(ids);
        String[] idsArray = ids.split(",");
        //删除窗口时需要校验该窗口是否在服务中
        for (String id : idsArray) {
            WindowBean windowById = windowService.getWindowById(Integer.valueOf(id));
            if (windowById == null) {
                return ResultUtils.ok("删除成功");
            }
            if (windowById.getIsUse().equals(CommonConstant.ISUSE) || windowById.getIsUse().equals(WindowUseStateEnum.Wait.getValue())) {
                return ResultUtils.error("删除窗口中存在正在服务的窗口");
            }
            //根据窗口查询窗口所绑定的终端
            VcDevBean vcdev = vcDevDao.getDeviceByWindowId(windowById.getId());
            if (vcdev != null) {
                if (VcDevStateEnum.Effective.getValue().equals(vcdev.getState())) {
                    vcDevDao.unbindDeviceAndModify(vcdev.getId());
                } else if (VcDevStateEnum.InUse.getValue().equals(vcdev.getState())) {
                    return ResultUtils.error("删除窗口所绑定终端正服务中");
                }
            }
        }
        return windowService.deleteWindow(idsArray);
    }

    @SysLogAnno("修改窗口")
    @RequiresPermissions("window:edit")
    @RequestMapping(value = "/updateWindow", method = RequestMethod.POST)
    public Map<String, Object> updateWindow(@RequestBody WindowVo windowVo) {
        ValidateUtil.validate(windowVo, UpdateGroup.class);
        WindowBean windowBean = new WindowBean();
        BeanUtils.copyProperties(windowVo, windowBean);
        return windowService.updateWindow(windowBean);
    }

    /**
     * 管理端查询窗口
     *
     * @return
     */
    @RequiresPermissions("window:query")
    @RequestMapping(value = "/getWindow", method = RequestMethod.POST)
    public Map<String, Object> getAllWindow(@RequestBody WindowVo windowVo) {
        ValidateUtil.validate(windowVo, QueryGroup.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //获取当前登录用户
        SysUserBean user = ShiroUtils.getUserEntity();
        //判断用户级别,超管以及统筹中心管理员可以查看所有窗口
        if (!CommonConstant.SUPER_ADMIN.equals(user.getType())) {
            paramMap.put("serviceKey", user.getServiceKey());
        }

        paramMap.put("pageNum", windowVo.getPageNum());
        paramMap.put("pageSize", windowVo.getPageSize());
        paramMap.put("windowName", windowVo.getWindowName());
        paramMap.put("deptId", windowVo.getDeptId());
        //0代表所有部门
        if (user.getDeptId() != CommonConstant.SUPER_ADMIN_DEPTID_TYPE) {
            paramMap.put("deptId", user.getDeptId());
        }
        return windowService.getWindow(paramMap);
    }

    @SysLogAnno("禁/启用窗口")
    @RequiresPermissions("window:changeStatus")
    @PostMapping("/lockWindow")
    public Map<String, Object> lockWindow(@RequestBody WindowVo windowVo) {
        ValidateUtil.validate(windowVo, LockGroup.class);
        return windowService.lockWindow(windowVo.getId(), windowVo.getState());
    }

    /**
     * 下拉框 窗口列表
     *
     * @return
     */
    @RequestMapping(value = "getListWindow", method = RequestMethod.POST)
    public Map<String, Object> getWindow(@RequestBody WindowVo windowVo) {
        ValidateUtil.validate(windowVo, DropDownGroup.class);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("serviceKey", windowVo.getServiceKey());
        paramMap.put("deptId", windowVo.getDeptId());
        return windowService.getListWindow(paramMap);
    }

    /**
     * 查询窗口详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getDetails", method = RequestMethod.GET)
    public Map<String, Object> getDetails(@RequestParam(value = "id") Integer id) {
        ValidateUtil.validate(id);
        WindowBean windowById = windowService.getWindowById(id);
        return ResultUtils.check(windowById);
    }

    /**
     * 将窗口置为空闲状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "setWindowFree", method = RequestMethod.GET)
    public Map<String, Object> setWindowFree(@RequestParam(value = "id") Integer id) {
        ValidateUtil.validate(id);
        return windowService.setWindowFree(id);
    }

    /**
     * 将窗口与业务员进行关联
     * id 窗口id
     */
    @RequestMapping(value = "addWindowToUser")
    public Map<String, Object> addWindowToUser(@RequestParam(value = "id") Integer id) {
        ValidateUtil.validate(id);
        return windowService.addWindowToUser(id);
    }

}
