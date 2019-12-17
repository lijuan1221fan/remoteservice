package com.visionvera.remoteservice.controller;


import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.validator.group.BusinessStateGroup;
import com.visionvera.common.validator.group.ChangeGroup;
import com.visionvera.common.validator.group.CompletGroup;
import com.visionvera.common.validator.group.GetGroup;
import com.visionvera.common.validator.group.NextGroup;
import com.visionvera.common.validator.group.PauseGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.pojo.BusinessVo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


/**
 * 业务处理器
 *
 * @author lijintao
 */
@RestController
@RequestMapping(value = "business")
public class BusinessController {

  private static Logger logger = LoggerFactory.getLogger(BusinessController.class);

  @Autowired
  private BusinessInfoService businessInfoService;

  /**
   * 暂停受理（暂停业务）
   *
   * @param businessId 业务id
   * @param finalStates 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @return
   */
  @RequestMapping(value = "pauseToDeal", method = RequestMethod.POST)
  public Map<String, Object> pauseToDeal(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, PauseGroup.class);
    return businessInfoService
        .pauseToDealAndModify(businessVo.getBusinessId(), businessVo.getFinalStates(),
            businessVo.getCauseIds(), businessVo.getRemarks(), businessVo.getClientInfo(),
            ShiroUtils.getUserEntity());
  }

  /**
   * 业务变更选择业务类型
   *
   * @param deptId,serviceKey
   * @return 根据业务员部门查询显示可变更的业务类型
   * @auther ljfan
   * @date 2018-10-31
   */
  @RequestMapping(value = "showBusinessType", method = RequestMethod.GET)
  public Map<String, Object> showBusinessType(Integer deptId) {
    return businessInfoService.getBusinessTypeByDeptId(deptId);
  }

  /**
   * 业务变更
   *
   * @param deptid,业务类型主键id:BusinessTypeId,业务详情id:businessType
   * @return 根据业务类型及部门查询显示可变更的业务
   * @auther ljfan
   * @date 2018-10-31
   */
  @RequestMapping(value = "getBusinessinfo", method = RequestMethod.POST)
  public Map<String, Object> getBusinessinfos(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, GetGroup.class);
    return businessInfoService.getBussinessInfoList(businessVo);
  }

  /**
   * 业务变更
   *
   * @param 业务主键 ：businessId，业务类型名称：describes，业务id：businessType
   * @return 业务变更处理
   * @auther ljfan
   * @date 2018-10-31
   */
  @SysLogAnno("业务变更")
  @RequestMapping(value = "changeBusiness", method = RequestMethod.POST)
  public Map<String, Object> changeBusinessInfo(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, ChangeGroup.class);
    return businessInfoService
        .changeBusinessInfo(businessVo.getBusinessId(), businessVo.getDescribes(),
            businessVo.getBusinessType(), businessVo.getCardName(), businessVo.getCardId());
  }

  /**
   * 身份证下一步接口
   *
   * @param businessId 业务id
   * @param clientInfo 用户身份证信息
   * @return
   */
  @RequestMapping(value = "nextStep", method = RequestMethod.POST)
  public Map<String, Object> nextStep(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, NextGroup.class);
    return businessInfoService
        .nextStepAndModify(businessVo.getBusinessId(), businessVo.getClientInfo());
  }

  /**
   * 完成业务接口
   *
   * @param businessId 业务id
   * @param finalStates 最终状态
   * @param causeIds 未处理原因id ，多个中间用逗号隔开
   * @param remarks 描述
   * @param clientInfo 客户信息
   * @return
   */
  @RequestMapping(value = "businessCompletion", method = RequestMethod.POST)
  public Map<String, Object> completeBusiness(@RequestBody BusinessVo businessVo) {
    try {
      ValidateUtil.validate(businessVo, CompletGroup.class);
      SysUserBean user = ShiroUtils.getUserEntity();
      return businessInfoService
          .completeBusinessAndModify(businessVo.getBusinessId(), businessVo.getFinalStates(),
              businessVo.getCauseIds(),
              businessVo.getRemarks(), businessVo.getClientInfo(), user);
    } catch (Exception e) {
      logger.error("保存业务错误。", e);
      return ResultUtils.error("保存业务错误。");
    }
  }

  /**
   * 查询业务列表
   *
   * @param pageNum 页码
   * @param pageSize 条数
   * @param businessInfo 查询参数
   * @return
   */
  @PostMapping(value = "getBusinessList")
  public Map<String, Object> getBusinessList(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, QueryGroup.class);
    return businessInfoService
        .getBusinessList(businessVo);
  }

  /**
   * 查询业务状态
   *
   * @param businessInfo
   */
  @RequestMapping(value = "getBusinessState", method = RequestMethod.POST)
  public Map<String, Object> getBusinessState(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, BusinessStateGroup.class);
    return businessInfoService.getBusinessState(businessVo.getBusinessInfo());
  }

  /**
   * 业务历史记录查询
   *
   * @param pageNum 页码
   * @param pageSize 条数
   * @param businessId 业务id
   * @return
   */
  @RequestMapping(value = "getBusinessLogList", method = RequestMethod.POST)
  public Map<String, Object> getBusinessLogList(@RequestBody BusinessVo businessVo) {
    ValidateUtil.validate(businessVo, QueryGroup.class);
    return businessInfoService
        .getBusinessLogList(businessVo.getPageNum(), businessVo.getPageSize(),
            businessVo.getBusinessId());
  }

  /**
   * 根据业务类型获取是否需要签名
   *
   * @param businessType 业务类型
   * @return
   * @author: lijintao
   * @Date: 2018/7/31
   */
  @RequestMapping(value = "isSign", method = RequestMethod.GET)
  public Map<String, Object> isSign(@RequestParam(value = "businessType") Integer businessType) {
    ValidateUtil.validate(businessType);
    return businessInfoService.isSign(businessType);
  }

  /**
   * 根据业务id获取客户信息
   *
   * @param businessId 业务id
   * @return
   */
  @RequestMapping(value = "getClientInfo", method = RequestMethod.GET)
  public Map<String, Object> getClientInfo(@RequestParam(value = "businessId") Integer businessId) {
    ValidateUtil.validate(businessId);
    return businessInfoService.getClientInfo(businessId);
  }

  /**
   * 根据业务id获取客户信息
   *
   * @param businessId 业务id
   * @return
   */
  @RequestMapping(value = "getBusinessDetail", method = RequestMethod.GET)
  public Map<String, Object> getBusinessDetail(@RequestParam(value = "businessId") Integer businessId) {
    ValidateUtil.validate(businessId);
    Map<String, Object> map = businessInfoService.getBusinessDetail(businessId);
    return map;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder, WebRequest request) {
    //转换日期
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // CustomDateEditor为自定义日期编辑器
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  /**
   * 业务统计根据部门ID查询业务类型
   *
   * @param deptId,serviceKey
   * @return 根据业务员部门查询显示可变更的业务类型
   * @auther ljfan
   * @date 2018-10-31
   */
  @RequestMapping(value = "selectBusinessTypeByDeptId", method = RequestMethod.GET)
  public Map<String, Object> selectBusinessTypeByDeptId(Integer deptId) {
    ValidateUtil.validate(deptId);
    return businessInfoService.selectBusinessTypeByDeptId(deptId);
  }
}
