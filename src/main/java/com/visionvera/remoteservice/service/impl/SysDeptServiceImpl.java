package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.common.enums.BusinessESignatureEnum;
import com.visionvera.common.enums.BusinessMaterialsPrintEnum;
import com.visionvera.common.enums.BusinessRemotePrintEnum;
import com.visionvera.common.enums.BusinessTypeEnum;
import com.visionvera.common.enums.BusinessTypeIsCustomEnum;
import com.visionvera.common.enums.BusinessTypeIsLeafEnum;
import com.visionvera.common.enums.BusinessTypeSupReceiptEnum;
import com.visionvera.common.enums.BusinessWholeTimeEnum;
import com.visionvera.common.enums.IsLeafEnum;
import com.visionvera.common.enums.PhotoConfigTypeEnum;
import com.visionvera.common.enums.SealLocationEnum;
import com.visionvera.common.enums.SingLocationEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysDeptTypeEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.NumberBean;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.NumberDao;
import com.visionvera.remoteservice.dao.PhotoConfigDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.SysUserDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.pojo.SysDeptVo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.service.SysDeptService;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ljfan on 2018/11/2.
 */
@Service("SysDeptServiceImpl")
public class SysDeptServiceImpl implements SysDeptService {

  private static Logger logger = LoggerFactory.getLogger(SysDeptServiceImpl.class);

  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
  @Resource
  private NumberDao numberDao;
  @Resource
  private BusinessTypeDao businessTypeDao;
  @Resource
  private MaterialsDao materialsDao;
  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private BusinessInfoDao businessInfoDao;
  @Resource
  private RedisUtils redisUtils;
  @Resource
  private WindowDao windowDao;
  @Resource
  private PhotoConfigDao photoConfigDao;
  @Resource
  private StorageUploadHelper storageUploadHelper;
  @Value("${temp.save.path}")
  private String tempPath;
  @Resource
  private BusinessTypeService businessTypeService;
  @Resource
  private BusinessInfoService businessInfoService;

  /**
   * 添加部门
   *
   * @param sysDeptBean
   * @return
   * @
   */
  @Override
  public Map<String, Object> addSysDept(SysDeptBean sysDeptBean, MultipartFile stampFile) {
    logger.info("添加部门参数：" + ToStringBuilder.reflectionToString(sysDeptBean));

    //校验名称
    int checkResult = sysDeptBeanDao.checkTheName(null, sysDeptBean.getDeptName());
    if (checkResult > 0) {
      logger.info("添加部门失败，部门名称已存在");
      return ResultUtils.error("添加部门失败，部门名称已存在");
    }

    //上传部门章
    if (stampFile != null && !stampFile.isEmpty()) {
      try {
        String originalFilename = stampFile.getOriginalFilename();
        String suffix = FilenameUtils.getExtension(originalFilename);
        String path = FileUploadUtil.uploadFile(tempPath, stampFile, suffix);
        File uploadFile = new File(path);
        StorageResVo storageResVo = storageUploadHelper.storageUpload(uploadFile, Integer.valueOf(StorageApi.FileType.IMG.getValue()));
        if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
          logger.info("存储网关返回文件路径为空");
          ResultUtils.error("存储网关返回文件路径为空");
        }
        if (uploadFile.exists()) {
          uploadFile.delete();
        }
        logger.info("部门章url：" + storageResVo.getFileUrl());
        sysDeptBean.setStampName(originalFilename);
        sysDeptBean.setStampUrl(storageResVo.getFileUrl());
      } catch (IOException e) {
        e.printStackTrace();
        logger.info("上传部门章失败：" + e);
        return ResultUtils.error("上传部门章失败，请重试");
      }
    }

    String numberprefix = sysDeptBeanDao.selectMaxNumberPrefix();
    //添加
    sysDeptBean.setNumberPrefix(StringUtil.nextChar(numberprefix));

    int insertResult = sysDeptBeanDao.insertSelective(sysDeptBean);
    if (insertResult == 0) {
      logger.info("添加部门失败");
      return ResultUtils.error("添加部门失败");
    }

    //添加号码迭代
    numberDao.insertSelective(new NumberBean(sysDeptBean.getServiceKey(), sysDeptBean.getId()));
    BusinessTypeBean businessTypeBean = new BusinessTypeBean();
    businessTypeBean.setDescribes("其他");
    businessTypeBean.setDeptId(sysDeptBean.getId());
    businessTypeBean.setType(1);
    int result = businessTypeDao.insertSelective(businessTypeBean);
    if (result == 0) {
      return ResultUtils.error("添加业务类型失败");
    }
    //添加综合业务
    BusinessTypeBean businessDetail = new BusinessTypeBean();
    businessDetail.setParentId(businessTypeBean.getId());
    businessDetail.setDescribes(sysDeptBean.getDeptName() + "-综合业务");
    businessDetail.setDeptId(sysDeptBean.getId());
    businessDetail.setGrade(CommonConstant.BUSINESS_GRADE);
    businessDetail.setIsLeaf(BusinessTypeIsLeafEnum.Yes.getValue());
    //办理流程：1：身份授权，2：材料收集，3：业务确认
    businessDetail.setBusinessFlow("1,2,3");
    businessDetail.setWholeTime(BusinessWholeTimeEnum.Yes.getValue());
    //默认所有月份
    businessDetail.setBusinessMonth("1,2,3,4,5,6,7,8,9,10,11,12");
    //默认任何日期
    businessDetail.setBusinessDay("1,31");
    businessDetail.seteSignature(BusinessESignatureEnum.Yes.getValue());
    businessDetail.setRemotePrint(BusinessRemotePrintEnum.Yes.getValue());
    businessDetail.setMaterialsPrint(BusinessMaterialsPrintEnum.Yes.getValue());
    //默认综合业务类型
    businessDetail.setType(Integer.valueOf(BusinessTypeEnum.Default.getValue()));
    businessDetail.setIsCustom(BusinessTypeIsCustomEnum.IsNotCUSTOM.getValue());
    businessDetail.setSupReceipt(BusinessTypeSupReceiptEnum.LocalReceipts.getValue());
    result = businessTypeDao.insertSelective(businessDetail);
    if (result == 0) {
      return ResultUtils.error("添加综合业务失败");
    }
    //设定签名盖章图片大小
    businessTypeService.insertPhotoConfig(PhotoConfigTypeEnum.ESignature.getValue(), businessDetail.getId(), null, null, SingLocationEnum.LocationHeight.getValue(), SingLocationEnum.LocationWidth.getValue());
    businessTypeService.insertPhotoConfig(PhotoConfigTypeEnum.Seal.getValue(), businessDetail.getId(), null, null, SealLocationEnum.LocationHeight.getValue(), SealLocationEnum.LocationWidth.getValue());
     //部门新建成功后：消息同步到一体机
    pushMessageToAndroid(sysDeptBean.getId());
    logger.info("添加部门成功");
    return ResultUtils.ok("添加部门成功");
  }
  //推送消息至叫号机
  private void pushMessageToAndroid(Integer deptId){
     List<ServiceCenterBean> centerBeans = serviceCenterDao.getChirdServiceList();
     if(CollectionUtils.isNotEmpty(centerBeans)){
         for(ServiceCenterBean bean:centerBeans){
           String proceing = businessInfoService.getCallNumberForAndroid(bean.getServiceKey(),deptId);
           businessInfoService.getSendMessageForAndroid(bean.getServiceKey(),proceing);
         }
     }
  }
  /**
   * 修改部门
   *
   * @param sysDeptBean
   * @return
   */
  @Override
  public Map<String, Object> updateSysDept(SysDeptBean sysDeptBean, MultipartFile stampFile) {
      logger.info("修改部门参数:" + ToStringBuilder.reflectionToString(sysDeptBean));
      //校验名称
      int checkResult = sysDeptBeanDao.checkTheName(sysDeptBean.getId(), sysDeptBean.getDeptName());
      if (checkResult > 0) {
          logger.info("修改部门失败，部门名称已存在");
          return ResultUtils.error("添加部门失败，部门名称已存在");
      }

      //上传部门章
      if (stampFile != null && !stampFile.isEmpty()) {
          try {
              String originalFilename = stampFile.getOriginalFilename();
              String suffix = FilenameUtils.getExtension(originalFilename);
              String path = FileUploadUtil.uploadFile(tempPath, stampFile, suffix);
              File uploadFile = new File(path);
              StorageResVo storageResVo = storageUploadHelper.storageUpload(uploadFile, Integer.valueOf(StorageApi.FileType.IMG.getValue()));
              if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
                  logger.info("存储网关返回文件路径为空");
                  ResultUtils.error("存储网关返回文件路径为空");
              }
              if (uploadFile.exists()) {
                  uploadFile.delete();
              }
              logger.info("部门章url：" + storageResVo.getFileUrl());
              sysDeptBean.setStampName(originalFilename);
              sysDeptBean.setStampUrl(storageResVo.getFileUrl());
          } catch (IOException e) {
              e.printStackTrace();
              logger.info("上传部门章失败：" + e);
              return ResultUtils.error("上传部门章失败，请重试");
          }
      }else if(StringUtil.isEmpty(sysDeptBean.getStampName())&&stampFile ==null){
        sysDeptBean.setStampName(null);
        sysDeptBean.setStampUrl(null);
      }

      //修改部门
      int updateResult = sysDeptBeanDao.updateByPrimaryKeySelective(sysDeptBean);
      if (updateResult == 0) {
          logger.info("修改部门失败");
          return ResultUtils.error("修改部门失败");
      }

      //查询综合业务类别
      BusinessTypeBean bean = new BusinessTypeBean();
      bean.setDeptId(sysDeptBean.getId());
      bean.setType(Integer.valueOf(BusinessTypeEnum.Default.getValue()));
      bean.setIsLeaf(Integer.valueOf(IsLeafEnum.Yes.getValue()));
      bean.setState(StateEnum.Effective.getValue());
      List<BusinessTypeBean> beans = businessTypeDao.getBusinessType(bean);
      if (beans.size() > 0) {
          BusinessTypeBean typeBean = beans.get(0);
          typeBean.setDescribes(sysDeptBean.getDeptName() + "-综合业务");
          int result = businessTypeDao.updateBusinessClasses(typeBean);
          if (result == 0) {
              return ResultUtils.error("更新综合业务失败");
          }
          logger.info("修改综合业务成功");
      }
      //部门新建成功后：消息同步到一体机
      pushMessageToAndroid(sysDeptBean.getId());
      logger.info("修改部门成功");
      return ResultUtils.ok("修改部门成功");
  }

  /**
   * 查询部门
   *
   * @param sysDeptVo
   * @return
   * @
   */
  @Override
  public Map<String, Object> getSysDeptList(SysDeptVo sysDeptVo) {
    if (sysDeptVo.getPageNum() != -1) {
      PageHelper.startPage(sysDeptVo.getPageNum(), sysDeptVo.getPageSize());
    }
    SysUserBean userBean = ShiroUtils.getUserEntity();
    if(!userBean.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)){
      sysDeptVo.setId(userBean.getDeptId());
    }
    List<SysDeptBean> list = sysDeptBeanDao.getDeptList(sysDeptVo);
    PageInfo<SysDeptBean> pageInfo = new PageInfo<SysDeptBean>(list);

    logger.info("查询部门成功");
    return ResultUtils.check("查询部门成功", pageInfo);
  }
  /**
   * 查询部门
   *
   * @param deptId
   * @return
   * @
   */
  @Override
  public Map<String, Object> getSysDeptById(Integer deptId) {
    SysDeptBean deptBean = sysDeptBeanDao.getDeptByDeptId(deptId);
    return ResultUtils.check("查询部门成功",deptBean);
  }

  /**
   * 查询部门  noPage
   *
   * @param id
   * @return
   * @
   */
  @Override
  public Map<String, Object> getSysDeptListNoPage(Integer id) {
    logger.info("查询部门参数：" + id);
    List<SysDeptBean> list = sysDeptBeanDao.getSysDeptListNoPage(id);
    logger.info("查询部门成功");
    return ResultUtils.ok("查询部门成功", list);
  }

  /**
   * @param sysDeptBean
   * @return java.util.Map<java.lang.String       ,       java.lang.Object>
   * @description: 修改状态
   * @author quboka
   * @date 2018/8/30 17:38
   */
  @Override
  public Map<String, Object> updateState(SysDeptBean sysDeptBean) {
    int result = sysDeptBeanDao.updateState(sysDeptBean);
    if (result == 0) {
      logger.info("修改状态失败");
      return ResultUtils.error("修改状态失败");
    } else {
      logger.info("修改状态成功");
      return ResultUtils.ok("修改状态成功");
    }
  }

  /**
   * 删除部门
   *
   * @param ids
   * @return
   * @
   */
  @Override
  public Map<String, Object> deleteSysDept(Integer[] ids) {
    logger.info("删除部门参数：" + ids);
    BusinessTypeBean businessTypeBean = new BusinessTypeBean();
    businessTypeBean.setDeptId(ids[0]);
    int count = businessInfoDao.getUnfinishedBusinessByDeptIds(ids);
    if (count > 0) {
      logger.info("删除部门失败,当前存在进行中或排队中的业务,请稍后再试");
      return ResultUtils.error("删除部门失败,当前存在进行中或排队中的业务,请稍后再试");
    }
    //业务类别删除判断
    Integer businessTypeInfoCount = businessTypeDao
        .getBusinessTypeInfoCount(ids[0]);
    //创建部门时默认添加其他业务其他
    if (businessTypeInfoCount > 0) {
      return ResultUtils.error("当前部门存在业务，请先删除业务再删除部门");
    }
    //删除材料
    List<Integer> materialsIds = materialsDao.selectIdByDeptId(ids);
    if (materialsIds != null && materialsIds.size() > CommonConstant.zero) {
      return ResultUtils.error("当前部门存在材料，请先删除材料再删除部门");
    }
    List<WindowBean> windowBeans = windowDao.getWindowByDeptIds(ids);
    if (windowBeans.size() > 0) {
      return ResultUtils.error("当前部门存在绑定窗口，请先删除已绑定的窗口再删除部门");
    }
    //删除用户（解绑终端、角色关联）
    List<Integer> userIds = sysUserDao.selectUserIdByDeptId(ids);
    if (userIds != null && userIds.size() > 0) {
      return ResultUtils.error("当前部门存在业务人员，请先删除业务人员再删除部门");
    }
    SysDeptVo sysDeptVo = new SysDeptVo();
    sysDeptVo.setIds(ids);
    List<SysDeptBean> deptList = sysDeptBeanDao.getDeptList(sysDeptVo);
    //删除队列
    BusinessInfo businessInfo = new BusinessInfo();
    ServiceCenterBean services = null;
    String queueKey = null;
    for (Integer deptId : ids) {
      businessInfo.setDeptId(deptId);
      List<BusinessInfo> businessInfos = businessInfoDao.selectByObject(businessInfo);
      if (businessInfos.size() > 0) {
        for (BusinessInfo info : businessInfos) {
          services = serviceCenterDao.getServiceCenter(info.getServiceKey());
          if (null != services) {
            queueKey = CommonConstant.QUEUE_KEY + services.getParentKey() + ":" + deptId;
            redisUtils.removePattern(queueKey);
          }

        }
      }
    }
    //删除部门
    int deleteResult = sysDeptBeanDao.deleteDeptByIds(ids);
    if (deleteResult == 0) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      logger.info("删除部门失败");
      return ResultUtils.error("删除部门失败");
    }
    //删除业务类别 & 删除业务详情
    businessTypeDao.deleteByDeptId(ids);
    for(Integer deptId:ids){
      pushMessageToAndroid(deptId);
    }
    logger.info("删除部门成功");
    return ResultUtils.ok("删除部门成功");
  }

  @Override
  public Map<String, Object> getSysDeptListNotInDelete() {
    List<SysDeptBean> list = sysDeptBeanDao.getSysDeptListNotInDelete();
    logger.info("查询部门成功");
    return ResultUtils.ok("查询部门成功", list);
  }

  @Override
  public Map<String, Object> getDeptStamp(Integer id) {
    SysDeptBean sysDeptBean = sysDeptBeanDao.getDeptStamp(id);
    if(sysDeptBean != null && sysDeptBean.getStampUrl() != null){
      return ResultUtils.ok("获取成功",sysDeptBean.getStampUrl());
    }
    return ResultUtils.error("获取失败");
  }

  @Override
  public Map<String, Object> getDeptStampByBusinessId(Integer businessId) {
    BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
    if(businessInfo != null && businessInfo.getDeptId() != null){
      SysDeptBean deptStampBean = sysDeptBeanDao.getDeptStamp(businessInfo.getDeptId());
      if(deptStampBean != null && deptStampBean.getStampUrl() != null){
        return ResultUtils.ok("查询成功",deptStampBean.getStampUrl());
      }
    }
    logger.info("该部门无印章");
    return ResultUtils.error("获取失败，请检查该部门是否已配置印章");
  }

  @Override
  public Map<String, Object> getDeptByDeptName(String deptName) {
    SysDeptBean deptByDeptName = sysDeptBeanDao.getDeptByDeptName(deptName);
    if (deptByDeptName != null) {
      return ResultUtils.ok("部门已经存在");
    }
    return ResultUtils.error("部门不存在");
  }

  @Override
  public Map<String, Object> deptListForChangeBusiness() {
    SysUserBean userBean = ShiroUtils.getUserEntity();
    SysDeptBean dept = new SysDeptBean();
    if(SysUserTypeEnum.WholeCenterSalesman.getValue().equals(userBean.getType())||
        SysUserTypeEnum.AuditCenterSalesman.getValue().equals(userBean.getType())){
      //当业务员为一窗综办业务员时
       if(userBean.getDeptId().equals(CommonConstant.SUPER_ADMIN_DEPTID_TYPE)){
         dept.setType(SysDeptTypeEnum.Yes.getValue());
       }else{
         //非一窗综办业务员
         dept.setType(SysDeptTypeEnum.No.getValue());
         dept.setId(userBean.getDeptId());
       }
    }
    List<SysDeptBean> list = sysDeptBeanDao.deptListForChangeBusiness(dept);
    return ResultUtils.ok("查询部门成功",list);
  }
}
