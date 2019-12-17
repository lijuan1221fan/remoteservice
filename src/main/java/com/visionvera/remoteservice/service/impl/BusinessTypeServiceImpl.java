package com.visionvera.remoteservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.common.enums.AndroidBusinessTypeEnum;
import com.visionvera.common.enums.BusinessESignatureEnum;
import com.visionvera.common.enums.BusinessInfoStateEnum;
import com.visionvera.common.enums.BusinessTypeEnum;
import com.visionvera.common.enums.BusinessTypeIsComprehensiveEnum;
import com.visionvera.common.enums.BusinessTypeIsCustomEnum;
import com.visionvera.common.enums.BusinessTypeIsLeafEnum;
import com.visionvera.common.enums.BusinessTypeSupReceiptEnum;
import com.visionvera.common.enums.BusinessWholeTimeEnum;
import com.visionvera.common.enums.PhotoConfigTypeEnum;
import com.visionvera.common.enums.SealLocationEnum;
import com.visionvera.common.enums.SingLocationEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.utils.RedisUtils;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.PhotoConfig;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.MaterialsType;
import com.visionvera.remoteservice.constant.OperationFlow;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.PhotoConfigDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.pojo.BusinessTypeVo;
import com.visionvera.remoteservice.pojo.ShowBusinessInfo;
import com.visionvera.remoteservice.service.BusinessInfoService;
import com.visionvera.remoteservice.service.BusinessTypeService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

/**
 * @Auther: quboka
 * @Date: 2018/8/24 09:45
 * @Description:
 */
@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    private static Logger logger = LoggerFactory.getLogger(BusinessTypeServiceImpl.class);

    @Resource
    private BusinessTypeDao businessTypeDao;

    @Resource
    private BusinessInfoDao businessInfoDao;

    @Resource
    private ServiceCenterDao serviceCenterDao;

    @Resource
    private MaterialsDao materialsDao;

    @Resource
    private PhotoConfigDao photoConfigDao;
    @Resource
    private SysDeptBeanDao sysDeptBeanDao;
    @Resource
    private BusinessInfoService businessInfoService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 加添业务类别
     * @author quboka
     * @date 2018/8/24 11:19
     */
    @Override
    public Map<String, Object> addBusinessClasses(BusinessTypeBean businessTypeBean) {
        SysUserBean userBean = ShiroUtils.getUserEntity();
        //业务中心校验
        if (SysUserTypeEnum.Admin.getValue().equals(userBean.getType())) {
            AssertUtil.isBlank("中心不能为空", businessTypeBean.getServiceKey());
        } else if (!SysUserTypeEnum.Admin.getValue().equals(userBean.getType())) {
          ServiceCenterBean bean = serviceCenterDao.getServiceCenter(businessTypeBean.getServiceKey());
            if (!userBean.getServiceKey().equals(businessTypeBean.getServiceKey())&&!userBean.getServiceKey().equals(bean.getParentKey())) {
                return ResultUtils.error("添加业务类别失败，请选择您所在的中心");
            }
        }
        if (!SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
            businessTypeBean.setType(Integer.valueOf(BusinessTypeEnum.Normal.getValue()));
        }
        logger.info("添加业务类别参数：" + businessTypeBean);
        //同一中心同一业务校验
        int count = businessTypeDao.checkoutClasses(businessTypeBean);
        if (count > CommonConstant.zero) {
            logger.info("添加业务类别失败，该类别已存在");
            return ResultUtils.error("添加业务类别失败，该类别已存在");
        }
        //不同中心父子级同一业务校验
        int countNum = businessTypeDao.checkDiddCenterClasses(businessTypeBean);
        if (countNum > 0) {
            logger.info("添加业务类别失败，该类别已存在");
            return ResultUtils.error("添加业务类别失败，该类别已存在");
        }
        SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(businessTypeBean.getDeptId());
        if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
            logger.info("添加失败，部门不存在");
            return ResultUtils.error("添加失败，部门不存在");
        }
        int result = businessTypeDao.insertSelective(businessTypeBean);
        if (result > CommonConstant.zero) {
            logger.info("添加业务类别成功");
            return ResultUtils.ok("添加业务类别成功");
        } else {
            logger.info("添加业务类别失败");
            return ResultUtils.error("添加业务类别失败");
        }
    }

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 修改类别
     * @author quboka
     * @date 2018/8/24 17:09
     */
    @Override
    public Map<String, Object> updateBusinessClasses(BusinessTypeBean businessTypeBean) {
        logger.info("修改业务类别参数：" + businessTypeBean);
        BusinessTypeBean bean = businessTypeDao.selectByPrimaryKey(businessTypeBean.getId());
        if (!bean.getServiceKey().equals(businessTypeBean.getServiceKey())) {
            return ResultUtils.error("修改业务类别失败，中心不可变更");
        }
        int count = businessTypeDao.checkoutClasses(businessTypeBean);
        if (count > CommonConstant.zero) {
            logger.info("修改业务类别失败，该类别已存在");
            return ResultUtils.error("修改业务类别失败，该类别已存在");
        }
        //不同中心父子级同一业务校验
        int countNum = businessTypeDao.checkDiddCenterClasses(businessTypeBean);
        if (countNum > 0) {
            logger.info("添加业务类别失败，该类别已存在");
            return ResultUtils.error("添加业务类别失败，该类别已存在");
        }
        //添加变更业务类型校验开始
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", businessTypeBean.getId());
        List<BusinessTypeBean> businessTypeBeans = businessTypeDao.getBusinessInfo(param);
        if (businessTypeBeans.size() > 0
                && businessTypeBeans.get(0).getDeptId() != businessTypeBean.getDeptId()) {
            logger.info("业务类别下存在业务，所以不可变更业务类别所属部门");
            return ResultUtils.error("业务类别下存在业务，所以不可变更业务类别所属部门");
        }
        //变更业务类型校验结束
        int result = businessTypeDao.updateBusinessClasses(businessTypeBean);
        if (result > 0) {
            logger.info("修改业务类别成功");
            return ResultUtils.ok("修改业务类别成功");
        } else {
            logger.info("修改业务类别失败");
            return ResultUtils.error("修改业务类别失败");
        }
    }

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 业务类别查询   1.当为admin时，显示所有数据，2.当为统筹管理员时，显示该账号及子中心的所有数据 3.当为审批管理员时，显示该账号中心下的数据
     * @author quboka
     * @date 2018/8/27 11:23
     */
    @Override
    public Map<String, Object> getBusinessClasses(BusinessTypeVo businessTypeVo) {
        List<BusinessTypeBean> alllist = null;
        Integer pageNum = businessTypeVo.getPageNum();
        Integer pageSize = businessTypeVo.getPageSize();
        BusinessTypeBean businessTypeBean = new BusinessTypeBean();
        businessTypeBean.setDescribes(businessTypeVo.getDescribes());
        businessTypeBean.setDeptId(businessTypeVo.getDeptId());
        businessTypeBean.setParentId(businessTypeVo.getParentId());
        businessTypeBean.setState(businessTypeVo.getState());
        businessTypeBean.setOfferNumberCheck(businessTypeVo.getOfferNumberCheck());
        businessTypeBean.setIsRests(businessTypeVo.getIsRests());
        businessTypeBean.setDescribes(businessTypeVo.getDescribes());
        businessTypeBean.setIsLeaf(businessTypeVo.getIsLeaf());
        businessTypeBean.setOfferNumberCheck(businessTypeVo.getOfferNumberCheck());
        businessTypeBean.setServiceName(businessTypeVo.getServiceName());
        SysUserBean userBean = ShiroUtils.getUserEntity();
        //1.当为admin账号时,拿所有业务数据
        if (SysUserTypeEnum.Admin.getValue().toString().equals(userBean.getType())) {
            if (pageNum != -1) {
                PageHelper.startPage(pageNum, pageSize);
            }
            businessTypeBean.setServiceKey(businessTypeVo.getServiceKey());
            alllist = businessTypeDao.getBusinessClasses(businessTypeBean);
        }
        //2.当为统筹管理员时，显示该账号及子中心的所有数据
        if (SysUserTypeEnum.WholeCenterAdmin.getValue().toString().equals(userBean.getType())) {
            //取得统筹中心下业务类型
            //业务材料取得业务类别
            //页面带查询serviceKey
            if (StringUtil.isNotEmpty(businessTypeVo.getServiceKey())) {
                businessTypeBean.setServiceKey(businessTypeVo.getServiceKey());
                alllist = businessTypeDao.getBusinessClasses(businessTypeBean);
            } else {
                //取得统筹中心下的所有审批中心下所有业务 业务列表展示
                List<String> serviceKeys = new ArrayList<>();
                String parentKey = userBean.getServiceKey();
                List<ServiceCenterBean> centerBeans = serviceCenterDao.getServiceCenterByParentKey(parentKey);
                for (ServiceCenterBean bean : centerBeans) {
                    serviceKeys.add(bean.getServiceKey());
                }
                serviceKeys.add(parentKey);
                businessTypeBean.setServiceKeys(serviceKeys);
                businessTypeBean.setServiceKey(null);
                if (pageNum != -1) {
                    PageHelper.startPage(pageNum, pageSize);
                }
                alllist = businessTypeDao.getBusinessClasses(businessTypeBean);
            }
        }
        //3.当为审批管理员时，统筹和admin管理员所建当业务类别只能查看
        if (SysUserTypeEnum.AuditCenterAdmin.getValue().toString().equals(userBean.getType())) {
            //业务材料取得业务类别
            //页面带查询serviceKey
            if (StringUtil.isNotEmpty(businessTypeVo.getServiceKey())) {
                businessTypeBean.setServiceKey(businessTypeVo.getServiceKey());
                alllist = businessTypeDao.getBusinessClasses(businessTypeBean);
            } else {
                //查询列表：取得审批管理员对于统筹中心
                List<String> serviceKeys = null;
                ServiceCenterBean bean = serviceCenterDao.getServiceCenter(userBean.getServiceKey());
                serviceKeys = new ArrayList<>();
                serviceKeys.add(bean.getParentKey());
                serviceKeys.add(userBean.getServiceKey());
                businessTypeBean.setServiceKeys(serviceKeys);
                businessTypeBean.setServiceKey(null);
                if (pageNum != -1) {
                    PageHelper.startPage(pageNum, pageSize);
                }
                alllist = businessTypeDao.getBusinessClasses(businessTypeBean);
                for (BusinessTypeBean typeBean : alllist) {
                    //审批中心只能查看，不能修改
                    if (bean.getParentKey().equals(typeBean.getServiceKey())) {
                        typeBean.setShow(false);
                    }
                }
            }
        }
        PageInfo<BusinessTypeBean> pageInfo = new PageInfo<BusinessTypeBean>(alllist);

        logger.info("查询业务类别成功");
        return ResultUtils.ok("查询业务类别成功", pageInfo);
    }

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 业务类别查询 取号机获取当前审批中心及对应统筹中心的业务列表
     * @author quboka
     * @date 2018/8/27 11:23
     */
    @Override
    public Map<String, Object> getTakeNumberBusinessClasses(BusinessTypeVo businessTypeVo) {

        List<BusinessTypeBean> list = null;
        BusinessTypeBean businessTypeBean = new BusinessTypeBean();
        businessTypeBean.setDeptId(businessTypeVo.getDeptId());
        businessTypeBean.setState(businessTypeVo.getState());
        businessTypeBean.setOfferNumberCheck(businessTypeVo.getOfferNumberCheck());
            List<String> serviceKeys = null;
            //根据村serviceKey取得审批中心
        ServiceCenterBean serviceCenter = serviceCenterDao.getParentServiceCenterByServiceKey(businessTypeVo.getServiceKey());
            if (null != serviceCenter) {
                //根据审批中心取得统筹中心的serviceKey
                serviceKeys = new ArrayList<>();
                serviceKeys.add(serviceCenter.getParentKey());
          serviceKeys.add(serviceCenter.getServiceKey());
            }
            businessTypeBean.setServiceKeys(serviceKeys);
      if (businessTypeVo.getPageNum() != -1) {
        PageHelper.startPage(businessTypeVo.getPageNum(), businessTypeVo.getPageSize());
      }
        //多中心业务类别取得
        if (StringUtil.isNotEmpty(businessTypeVo.getServiceKey())&&0 == businessTypeVo.getParentId()) {
            list = businessTypeDao.getBusinessClasseByServiceKeys(businessTypeBean);
        } else {
            //业务详情取得
            businessTypeBean.setParentId(businessTypeVo.getParentId());
            list = businessTypeDao.getBusinessClasses(businessTypeBean);

        }
        PageInfo<BusinessTypeBean> pageInfo = new PageInfo<BusinessTypeBean>(list);

        logger.info("查询业务类别成功");
        return ResultUtils.ok("查询业务类别成功", pageInfo);
    }

    @Override
    public Map<String, Object> getBusinessDetailById(Integer businessDetailId) {
        BusinessTypeBean businessTypeBean = businessTypeDao.getBusinessDetailById(businessDetailId);
        //查询签名/盖章自定义位置
        setBusinessTypeBeanForPhotoConfig(businessTypeBean);

        return ResultUtils.check("查询业务详情成功", businessTypeBean);
    }

    //查询签名/盖章自定义位置
    @Override
    public void setBusinessTypeBeanForPhotoConfig(BusinessTypeBean businessTypeBean) {
        List<PhotoConfig> photoConfigs = photoConfigDao.selectByBusinessDetailId(businessTypeBean.getId());
        if (photoConfigs.size() > 0) {
            for (PhotoConfig photoConfig : photoConfigs) {
                //签名数据获取
                if (PhotoConfigTypeEnum.ESignature.getValue().equals(photoConfig.getType())) {
                    businessTypeBean.setSignX(photoConfig.getX());
                    businessTypeBean.setSignY(photoConfig.getY());
                }
                //盖章数据获取
                if (PhotoConfigTypeEnum.Seal.getValue().equals(photoConfig.getType())) {
                    businessTypeBean.setStampX(photoConfig.getX());
                    businessTypeBean.setStampY(photoConfig.getY());
                }
            }
        }
    }

    @Override
    public Map<String, Object> getBusinessTypeById(Integer businessTypeId) {
        return ResultUtils.check("查询业务类别成功", businessTypeDao.getBusinessTypeById(businessTypeId));
    }

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 业务类别列表查询
     * @author ljfan
     * @date 2018/11/14 11:23
     */
    @Override
    public Map<String, Object> getBusinessTypeInfoById(Integer id) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = businessTypeDao.getBusinessTypeInfoById(id);
        Map<String, Object> typeMap = null;
        List<Map> typeList = null;
        List<Map> materialsList = new ArrayList<>();
        Map<String, Object> typeListMap = null;
        if (CollectionUtils.isEmpty(list)) {
            return ResultUtils.error("未查询到材料");
        }
        // 材料类型列表
        for (MaterialsType type : MaterialsType.values()) {
            typeList = new ArrayList<>();
            //遍历材料类型
            for (Map map : list) {
                typeListMap = new HashMap<String, Object>();
                if (type.getValue().equals(map.get("materials_type"))) {
                    typeListMap.put(type.getName(), type.getValue());
                    typeListMap.put("id", map.get("id"));
                    typeListMap.put("materialsName", map.get("materials_name"));
                }
                if (typeListMap.size() > CommonConstant.zero) {
                    typeList.add(typeListMap);
                }

            }
            if (typeList.size() > CommonConstant.zero) {
                typeMap = new HashMap<String, Object>();
                typeMap.put(type.getName(), type.getValue());
                typeMap.put(type.name(), typeList);
                materialsList.add(typeMap);
            }


        }
        returnMap.put("materialsType", materialsList);
        logger.info("查询业务类别成功");
        return ResultUtils.ok("查询业务类别成功", returnMap);
    }

    /**
     * @param [paramMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 业务类别列表查询
     * @author ljfan
     * @date 2018/11/14 11:23
     */
    @Override
    public Map<String, Object> getBusinessClassesNoPage(BusinessTypeBean businessTypeBean) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<String> serviceKeys = new ArrayList<>();
        serviceKeys.add(businessTypeBean.getServiceKey());
        ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(businessTypeBean.getServiceKey());
        if (!CommonConstant.PARENT_KEY.equals(serviceCenterBean.getParentKey())) {
            serviceKeys.add(serviceCenterBean.getParentKey());
        }
        businessTypeBean.setServiceKeys(serviceKeys);
        businessTypeBean.setServiceKey(null);
        List<BusinessTypeBean> list = businessTypeDao.getBusinessType(businessTypeBean);
        Map<String, Object> operationMap = new HashMap<String, Object>();
        for (OperationFlow flow : OperationFlow.values()) {
            operationMap.put(flow.getName(), flow.getValue());
        }
        returnMap.put("OperationFlow", operationMap);
        returnMap.put("businessList", list);
        logger.info("查询业务类别成功");
        return ResultUtils.ok("查询业务类别成功", returnMap);
    }

    /**
     * @param [businessTypeBean]
     * @param typeMaterialsRel
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 添加业务详情
     * @author quboka
     * @date 2018/8/27 15:26
     */
    @Override
    public Map<String, Object> addBusinessType(BusinessTypeVo businessTypeVo) {
        BusinessTypeBean businessTypeBean = new BusinessTypeBean();
        BeanUtils.copyProperties(businessTypeVo, businessTypeBean);
        String materialsIds = businessTypeVo.getMaterialsIds();
        String[] materialsIdList = checkMaterialDep(businessTypeBean, materialsIds);
        SysUserBean userBean = ShiroUtils.getUserEntity();
        //业务中心校验
        if (SysUserTypeEnum.Admin.getValue().equals(userBean.getType())) {
            AssertUtil.isBlank("中心不能为空", businessTypeBean.getServiceKey());
        } else if (!SysUserTypeEnum.Admin.getValue().equals(userBean.getType())) {
          ServiceCenterBean bean = serviceCenterDao.getServiceCenter(businessTypeBean.getServiceKey());
          if (!userBean.getServiceKey().equals(businessTypeBean.getServiceKey()) && !userBean.getServiceKey().equals(bean.getParentKey())) {
                return ResultUtils.error("添加业务类别失败，请选择您所在的中心");
            }
        }
        logger.info("添加业务详情参数：" + ToStringBuilder.reflectionToString(businessTypeBean));
        SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(businessTypeBean.getDeptId());
        if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
            logger.info("添加失败，部门不存在");
            return ResultUtils.error("添加失败，部门不存在");
        }
        int count = businessTypeDao.checkoutType(businessTypeBean);
        if (count > 0) {
            logger.info("添加业务详情失败，该业务详情已存在");
            return ResultUtils.error("添加业务详情失败，该业务详情已存在");
        }
        businessTypeBean.setIsCustom(businessTypeVo.getIsCustom());
        //判断是否为综合业务
        boolean isComprehensive=IsComprehensive(businessTypeVo);
        if(!isComprehensive){
          return ResultUtils.error("添加业务详情失败，所属中心不是统筹中心");
        }
        businessTypeBean.setIsComprehensive(businessTypeVo.getIsComprehensive());
        //当业务为本地查看回执单模式时：签名以及盖章均有值时，设定远程打印
        if (businessTypeVo.getSupReceipt() != null && BusinessTypeSupReceiptEnum.LocalReceipts.getValue().equals(businessTypeVo.getSupReceipt())) {
            businessTypeBean.setRemotePrint(CommonConstant.one);
        } else {
            businessTypeBean.setRemotePrint(CommonConstant.zero);
        }
        //当有材料上传、本地查看回执单时，为全时段
        updateBusinessTypeBeanForBusinessFlow(materialsIds,businessTypeBean);
        businessTypeBean.setIsLeaf(BusinessTypeIsLeafEnum.Yes.getValue());
        int insertResult = businessTypeDao.addBusiness(businessTypeBean);
        if (insertResult != 1) {
            logger.info("添加业务详情失败");
            return ResultUtils.error("添加业务详情失败");
        }
        if (businessTypeVo.getIsCustom() != null && businessTypeVo.getIsCustom().equals(BusinessTypeIsCustomEnum.IsCUSTOM.getValue())) {
            //配置业务签名位置
            if (businessTypeVo.getSignX() != null) {
                insertPhotoConfig(PhotoConfigTypeEnum.ESignature.getValue(), businessTypeBean.getId(), businessTypeVo.getSignX(), businessTypeVo.getSignY(), SingLocationEnum.LocationHeight.getValue(), SingLocationEnum.LocationWidth.getValue());
            }
            //配置业务盖章位置
            if (businessTypeVo.getStampX() != null) {
                insertPhotoConfig(PhotoConfigTypeEnum.Seal.getValue(), businessTypeBean.getId(), businessTypeVo.getStampX(), businessTypeVo.getStampY(), SealLocationEnum.LocationHeight.getValue(), SealLocationEnum.LocationWidth.getValue());
            }
        } else if (businessTypeVo.getIsCustom() != null && businessTypeVo.getIsCustom().equals(BusinessTypeIsCustomEnum.IsNotCUSTOM.getValue())) {
            insertPhotoConfig(PhotoConfigTypeEnum.ESignature.getValue(), businessTypeBean.getId(), null, null, SingLocationEnum.LocationHeight.getValue(), SingLocationEnum.LocationWidth.getValue());
            insertPhotoConfig(PhotoConfigTypeEnum.Seal.getValue(), businessTypeBean.getId(), null, null, SealLocationEnum.LocationHeight.getValue(), SealLocationEnum.LocationWidth.getValue());
        }
        if (materialsIdList != null) {
            logger.info("业务详情关联材料ids: " + materialsIds);
            businessTypeDao.insertRel(businessTypeBean.getId(), materialsIdList);
        }

        logger.info("添加业务详情成功");
        return ResultUtils.ok("添加业务详情成功");
    }

    //向photoconfig表插入数据
    @Override
    public void insertPhotoConfig(Integer photoConfigType, Integer businessTypeId, Integer x, Integer y, Integer h, Integer w) {
        PhotoConfig photoConfig = new PhotoConfig();
        photoConfig.setType(photoConfigType);
        photoConfig.setBusinessType(businessTypeId);
        photoConfig.setX(x);
        photoConfig.setY(y);
        photoConfig.setHeight(h);
        photoConfig.setWidth(w);
        photoConfigDao.insertSelective(photoConfig);
    }

    private String[] checkMaterialDep(BusinessTypeBean businessTypeBean, String materialsIds) {
        if (StringUtils.isNotBlank(materialsIds)) {
            String[] materialsIdArray = materialsIds.split(",");
            if (materialsIdArray.length > 0) {
                if (materialsDao
                        .getCountNotInDeptByMaterialId(materialsIdArray, businessTypeBean.getDeptId()) > 0) {
                    logger.info("所选材料与所选部门不匹配");
                    throw new MyException("所选材料与所选部门不匹配");
                }
                return materialsIdArray;
            }
        }
        return null;
    }

    /**
     * @param businessTypeVo
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 修改业务详情
     * @author quboka
     * @date 2018/8/27 15:25
     */
    @Override
    public Map<String, Object> updateBusinessType(BusinessTypeVo businessTypeVo) {
        BusinessTypeBean businessTypeBean = new BusinessTypeBean();
        BeanUtils.copyProperties(businessTypeVo, businessTypeBean);
        BusinessInfo info = new BusinessInfo();
        info.setState(BusinessInfoStateEnum.Untreated.getValue());
        info.setBusinessType(businessTypeVo.getId());
        List<BusinessInfo> checkBusinessInfo = businessInfoDao.getBusinessList(info);
        if(checkBusinessInfo.size() > 0){
          return ResultUtils.error("该业务已有待办业务，禁止修改");
        }
        info = new BusinessInfo();
        info.setState(BusinessInfoStateEnum.Processing.getValue());
        info.setBusinessType(businessTypeVo.getId());
        checkBusinessInfo = businessInfoDao.getBusinessList(info);
        if(checkBusinessInfo.size() > 0){
           return ResultUtils.error("该业务正在办理中，禁止修改");
        }
        String materialsIds = businessTypeVo.getMaterialsIds();
        String[] materialsIdList = checkMaterialDep(businessTypeBean, materialsIds);
        BusinessTypeBean bean = businessTypeDao.selectByPrimaryKey(businessTypeBean.getId());
        if (!bean.getServiceKey().equals(businessTypeBean.getServiceKey())) {
            return ResultUtils.error("修改业务类别失败，中心不可变更");
        }
        logger.info("修改业务详情参数：" + ToStringBuilder.reflectionToString(businessTypeBean));
        int count = businessTypeDao.checkoutType(businessTypeBean);
        if (count > 0) {
            logger.info("修改业务详情失败，该业务详情已存在");
            return ResultUtils.error("修改业务详情失败，该业务详情已存在");
        }
        businessTypeBean.setIsCustom(businessTypeVo.getIsCustom());
        //判断是否为综合业务
        boolean isComprehensive=IsComprehensive(businessTypeVo);
        if(!isComprehensive){
          return ResultUtils.error("添加业务详情失败，所属中心不是统筹中心");
        }
        businessTypeBean.setIsComprehensive(businessTypeVo.getIsComprehensive());
        //当业务为本地查看回执单模式时：签名以及盖章均有值时，设定远程打印
        updateBusinessTypeBeanForBusinessFlow(materialsIds,businessTypeBean);
        businessTypeBean.setIsLeaf(BusinessTypeIsLeafEnum.Yes.getValue());
        int insertResult = businessTypeDao.updateByPrimaryKeySelective(businessTypeBean);
        if (insertResult != 1) {
            logger.info("修改业务详情失败");
            return ResultUtils.error("页面数据已过期，请刷新后重试");
        }

        PhotoConfig signPhotoConfig = photoConfigDao.selectByBusinessType(businessTypeVo.getId(), PhotoConfigTypeEnum.ESignature.getValue());
        PhotoConfig stampPhotoConfig = photoConfigDao.selectByBusinessType(businessTypeVo.getId(), PhotoConfigTypeEnum.Seal.getValue());

        if (businessTypeVo.getIsCustom() != null && businessTypeVo.getIsCustom().equals(BusinessTypeIsCustomEnum.IsCUSTOM.getValue())) {
            //配置业务签名位置
            if (businessTypeVo.getSignX() != null) {
                if (signPhotoConfig != null) {
                    updateSignPhotoConfig(businessTypeVo, businessTypeBean);
                } else {
                    insertPhotoConfig(PhotoConfigTypeEnum.ESignature.getValue(), businessTypeBean.getId(), businessTypeVo.getSignX(), businessTypeVo.getSignY(), SingLocationEnum.LocationHeight.getValue(), SingLocationEnum.LocationWidth.getValue());
                }
            } else {
                if (signPhotoConfig != null) {
                    photoConfigDao.deleteByPrimaryKey(signPhotoConfig.getId());
                }
            }
            //配置业务盖章位置
            if (businessTypeVo.getStampX() != null) {
                if (stampPhotoConfig != null) {
                    updateStampPhotoConfig(businessTypeVo, businessTypeBean);
                } else {
                    insertPhotoConfig(PhotoConfigTypeEnum.Seal.getValue(), businessTypeBean.getId(), businessTypeVo.getStampX(), businessTypeVo.getStampY(), SealLocationEnum.LocationHeight.getValue(), SealLocationEnum.LocationWidth.getValue());
                }
            } else {
                if (stampPhotoConfig != null) {
                    photoConfigDao.deleteByPrimaryKey(stampPhotoConfig.getId());
                }
            }
        } else if (businessTypeVo.getIsCustom() != null && businessTypeVo.getIsCustom().equals(BusinessTypeIsCustomEnum.IsNotCUSTOM.getValue())) {
            if (signPhotoConfig != null) {
                updateSignPhotoConfig(businessTypeVo, businessTypeBean);
            } else {
                insertPhotoConfig(PhotoConfigTypeEnum.ESignature.getValue(), businessTypeBean.getId(), null, null, SingLocationEnum.LocationHeight.getValue(), SingLocationEnum.LocationWidth.getValue());
            }
            if (stampPhotoConfig != null) {
                updateStampPhotoConfig(businessTypeVo, businessTypeBean);
            } else {
                insertPhotoConfig(PhotoConfigTypeEnum.Seal.getValue(), businessTypeBean.getId(), null, null, SealLocationEnum.LocationHeight.getValue(), SealLocationEnum.LocationWidth.getValue());
            }
        } else if (businessTypeVo.getIsCustom() == null) {
            if (stampPhotoConfig != null) {
                photoConfigDao.deleteByPrimaryKey(stampPhotoConfig.getId());
            }
            if (signPhotoConfig != null) {
                photoConfigDao.deleteByPrimaryKey(signPhotoConfig.getId());
            }

        }
        logger.info("清空业务详情与材料关联关系");
        businessTypeDao.deleteRel(businessTypeBean.getId());

        if (materialsIdList != null) {
            logger.info("业务详情关联材料ids: " + materialsIds);
            businessTypeDao.insertRel(businessTypeBean.getId(), materialsIdList);
        }
        logger.info("修改业务详情成功");
        return ResultUtils.ok("修改业务详情成功");
    }

    private void updateBusinessTypeBeanForBusinessFlow(String materialsIds,BusinessTypeBean businessTypeBean){
      //当有材料上传、本地查看回执单时，为全时段
      if (StringUtils.isNotBlank(materialsIds) && BusinessESignatureEnum.Yes.getValue().equals(businessTypeBean.geteSignature())) {
        businessTypeBean.setWholeTime(BusinessWholeTimeEnum.Yes.getValue());
        //工作流程1 身份授权、2 材料收集、3 业务确认
        businessTypeBean.setBusinessFlow("1,2,3");

      } else if (StringUtils.isNotBlank(materialsIds) && BusinessESignatureEnum.No.getValue().equals(businessTypeBean.geteSignature())) {
        //当有材料上传、单独上传电子签名模式，业务流程仅1，2
        businessTypeBean.setWholeTime(BusinessWholeTimeEnum.No.getValue());
        businessTypeBean.setBusinessFlow("1,2");

      }else if(StringUtils.isNotBlank(materialsIds)){
        //当有材料上传、单独上传电子签名模式，非全时段
        businessTypeBean.setWholeTime(BusinessWholeTimeEnum.No.getValue());
        businessTypeBean.setBusinessFlow("1,2");
      } else if(StringUtils.isBlank(materialsIds) && !BusinessTypeSupReceiptEnum.NotReceipt.getValue().equals(businessTypeBean.getSupReceipt())){
        //当有材料上传、单独上传电子签名模式，非全时段
        businessTypeBean.setWholeTime(BusinessWholeTimeEnum.No.getValue());
        businessTypeBean.setBusinessFlow("1,3");
      }else if(StringUtils.isBlank(materialsIds) && BusinessTypeSupReceiptEnum.NotReceipt.getValue().equals(businessTypeBean.getSupReceipt())) {
        //当有材料上传、单独上传电子签名模式，非全时段
        businessTypeBean.setWholeTime(BusinessWholeTimeEnum.No.getValue());
        businessTypeBean.setBusinessFlow("1");
      }
    }
    /**
     * 更新stampConfig
     *
     * @param businessTypeVo
     * @param businessTypeBean
     */
    private void updateStampPhotoConfig(BusinessTypeVo businessTypeVo, BusinessTypeBean businessTypeBean) {
        PhotoConfig stampPhotoConfig = photoConfigDao.selectByBusinessType(businessTypeVo.getId(), PhotoConfigTypeEnum.Seal.getValue());
        if (stampPhotoConfig == null) {
            stampPhotoConfig = new PhotoConfig();
        }
        stampPhotoConfig.setType(PhotoConfigTypeEnum.Seal.getValue());
        stampPhotoConfig.setBusinessType(businessTypeBean.getId());
        stampPhotoConfig.setX(businessTypeVo.getStampX());
        stampPhotoConfig.setY(businessTypeVo.getStampY());
        stampPhotoConfig.setHeight(SealLocationEnum.LocationHeight.getValue());
        stampPhotoConfig.setWidth(SealLocationEnum.LocationWidth.getValue());
        photoConfigDao.updateByPrimaryKey(stampPhotoConfig);
    }

    /**
     * 更新signConfig
     *
     * @param businessTypeVo
     * @param businessTypeBean
     */
    private void updateSignPhotoConfig(BusinessTypeVo businessTypeVo, BusinessTypeBean businessTypeBean) {
        PhotoConfig signPhotoConfig = photoConfigDao.selectByBusinessType(businessTypeVo.getId(), PhotoConfigTypeEnum.ESignature.getValue());
        if (signPhotoConfig == null) {
            signPhotoConfig = new PhotoConfig();
        }
        signPhotoConfig.setBusinessType(businessTypeBean.getId());
        signPhotoConfig.setType(PhotoConfigTypeEnum.ESignature.getValue());
        signPhotoConfig.setX(businessTypeVo.getSignX());
        signPhotoConfig.setY(businessTypeVo.getSignY());
        signPhotoConfig.setHeight(SingLocationEnum.LocationHeight.getValue());
        signPhotoConfig.setWidth(SingLocationEnum.LocationWidth.getValue());
        photoConfigDao.updateByPrimaryKey(signPhotoConfig);
    }

    /**
     * @param [paramsMap]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 修改状态
     * @author quboka
     * @date 2018/8/30 17:49
     */
    @Override
    public Map<String, Object> updateState(BusinessTypeBean businessTypeBean) {
        int result = businessTypeDao.updateState(businessTypeBean);
        if (result == 1) {
            logger.info("修改状态成功");
            return ResultUtils.ok("修改状态成功");
        }
        logger.info("修改状态失败");
        return ResultUtils.error("修改状态失败");
    }

    /**
     * @param [ids]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 删除业务详情
     * @author quboka
     * @date 2018/8/31 10:56
     */
    @Override
    public Map<String, Object> deleteBusinessType(Integer[] ids) {

        int count = businessInfoDao.getUnfinishedBusinessByTypeIds(ids);
        if (count > CommonConstant.zero) {
            logger.info("删除业务详情失败,当前存在进行中或排队中的业务,请稍后再试");
            return ResultUtils.error("删除业务详情失败,当前存在进行中或排队中的业务,请稍后再试");
        }

        businessTypeDao.deleteRelByIds(ids);

        int result = businessTypeDao.deleteByIds(ids);
        if (result == CommonConstant.zero) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("删除业务详情失败");
            return ResultUtils.error("删除业务详情失败");
        }
        logger.info("删除业务详情成功");
        return ResultUtils.ok("删除业务详情成功");
    }

    /**
     * @param [ids]
     * @param isDel 1：删除归属业务  0：移到其他类别中
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @description: 删除类别
     * @author quboka
     * @date 2018/8/24 17:28
     */
    @Override
    public Map<String, Object> deleteBusinessClasses(Integer[] ids, Integer isDel) {

        if (isDel == CommonConstant.zero) {
            for (Integer id : ids) {
                Integer deptId = businessTypeDao.getDeptIdByid(id);
                List<BusinessTypeBean> businessType = businessTypeDao.getDefaultClassesByDeptId(deptId);
                for (BusinessTypeBean bean : businessType) {
                    businessTypeDao.updateParentId(bean.getId(), id);
                }

            }
        } else if (isDel == CommonConstant.businessOwn) {
            List<Integer> typeIds = businessTypeDao.getTypeIdsByClassesIds(ids);
            if (typeIds != null && typeIds.size() > CommonConstant.zero) {

                int count = businessInfoDao
                        .getUnfinishedBusinessByTypeIds(typeIds.toArray(new Integer[CommonConstant.zero]));
                if (count > CommonConstant.zero) {
                    logger.info("删除业务类别失败,当前存在进行中或排队中的业务,请稍后再试");
                    return ResultUtils.error("删除业务类别失败,当前存在进行中或排队中的业务,请稍后再试");
                }

                businessTypeDao.deleteByIds(typeIds.toArray(new Integer[CommonConstant.zero]));
                businessTypeDao.deleteRelByIds(typeIds.toArray(new Integer[CommonConstant.zero]));
            }
        } else {
            logger.info("删除业务类别失败,参数错误");
            return ResultUtils.error("删除业务类别失败,参数错误");
        }

        int result = businessTypeDao.deleteBusinessClasses(ids);
        if (result == CommonConstant.zero) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("删除业务类别失败");
            return ResultUtils.error("删除业务类别失败");
        }

        logger.info("删除业务类别成功");
        return ResultUtils.ok("删除业务类别成功");
    }


    /**
     * 校验业务详情
     *
     * @param type 业务详情
     * @return
     */
    @Override
    public Map<String, Object> checkoutType(BusinessTypeBean type) {
        logger.info("校验业务详情参数：" + ToStringBuilder.reflectionToString(type));
        /**
         * 业务详情不需要serviceKey
         *      if (StringUtil.isEmptyByArr(type.getServiceKey(), type.getDescribes())
         */
        if (StringUtil.isEmptyByArr(type.getDescribes()) || type.getDeptId() == null) {
            logger.info("校验业务详情失败，缺少参数");
            return ResultUtils.error("校验业务详情失败，缺少参数");
        }
        int count = businessTypeDao.checkoutType(type);
        if (count > CommonConstant.zero) {
            logger.info("校验业务详情失败，该业务详情已存在");
            return ResultUtils.error("校验业务详情失败，该业务详情已存在");
        } else {
            logger.info("校验业务详情成功");
            return ResultUtils.ok("校验业务详情成功");
        }
    }

    @Override
    public Map<String, Object> updateBusinessTypeState(BusinessTypeVo businessTypeVo) {
        int count = businessTypeDao.updateBusinessTypeState(businessTypeVo);
        if (count > CommonConstant.zero) {
            logger.info("启用/禁用服务类别成功");
            return ResultUtils.ok("启用/禁用服务类别成功");
        } else {
            logger.info("启用/禁用服务类别失败");
            return ResultUtils.error("启用/禁用服务类别");
        }
    }

    //安卓端初始化接口
    @Override
    public Map<String, Object> androidShow(BusinessTypeVo businessTypeVo) {
      List<ShowBusinessInfo> showInfos;
        //单部门处理
        if (AndroidBusinessTypeEnum.DepartmentalProcess.getValue().equals(businessTypeVo.getShowType())) {
            //消息推送
            //获取下一个待办理的号
           showInfos = businessInfoService.getShowBusinessInfoList(businessTypeVo.getServiceKey(), "");
        } else {
            //一窗综办时，取得所有的业务代办数
          showInfos = businessInfoService.getShowBusinessInfosForAndroid(CommonConstant.SUPER_ADMIN_DEPTID_TYPE, businessTypeVo.getServiceKey());
        }
      return ResultUtils.check(showInfos);
    }
    //判断是否为支持综合业务办理
    private boolean IsComprehensive(BusinessTypeVo businessTypeVo){
      //判断是否为支持综合办理业务
      if(null !=businessTypeVo.getIsComprehensive()&&BusinessTypeIsComprehensiveEnum.Yes.getValue().equals(businessTypeVo.getIsComprehensive())){
        ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceCenter(businessTypeVo.getServiceKey());
        if(serviceCenterBean != null && CommonConstant.PARENT_KEY.equals(serviceCenterBean.getParentKey()) && CommonConstant.CENTER_ONE.equals(serviceCenterBean.getType())){
          businessTypeVo.setIsComprehensive(BusinessTypeIsComprehensiveEnum.Yes.getValue());
          return true;
        }else{
          businessTypeVo.setIsComprehensive(BusinessTypeIsComprehensiveEnum.No.getValue());
          return false;
        }
      }else if(null !=businessTypeVo.getIsComprehensive()&&BusinessTypeIsComprehensiveEnum.No.getValue().equals(businessTypeVo.getIsComprehensive())){
        businessTypeVo.setIsComprehensive(BusinessTypeIsComprehensiveEnum.No.getValue());
        return true;
      }else{
          businessTypeVo.setIsComprehensive(null);
          return true;
      }
    }
}
