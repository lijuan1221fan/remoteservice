package com.visionvera.remoteservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.api.handler.constant.ContentApi;
import com.visionvera.api.handler.constant.StorageApi.FileType;
import com.visionvera.common.enums.BusinessTypeIsComprehensiveEnum;
import com.visionvera.common.enums.BusinessTypeIsFormEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.common.enums.SysUserTypeEnum;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.SysUserBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.constant.MaterialsType;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.MaterialsDao;
import com.visionvera.remoteservice.dao.PhotoConfigDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.pojo.MaterialsVo;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.MaterialsService;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.ShiroUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: quboka
 * @Date: 2018/8/23 13:39
 * @Description:
 */
@Service
public class MaterialsServiceImpl implements MaterialsService {

  private static Logger logger = LoggerFactory.getLogger(MaterialsServiceImpl.class);

  @Autowired
  private MaterialsDao materialsDao;
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
  @Resource
  private StorageUploadHelper storageUploadHelper;
  @Value("${temp.save.docpath}")
  private String tempPath;
  @Resource
  private PhotoConfigDao photoConfigDao;
  @Resource
  private BusinessTypeDao businessTypeDao;

  /**
   * @param [materials]
   * @return java.util.Map<java.lang.String       ,       java.lang.Object>
   * @description: 添加材料
   * @author quboka
   * @date 2018/8/23 13:41
   */
  @Override
  public Map<String, Object> addMaterials(Materials materials) {
    logger.info("添加材料参数：" + ToStringBuilder.reflectionToString(materials));
    /**
     * 当admin操作数据时，serviceKey值为0
     */
    SysDeptBean deptBean = sysDeptBeanDao.getDeptInfo(materials.getDeptId());
    if (deptBean == null || deptBean.getState().equals(StateEnum.Invalid.getValue())) {
      logger.info("添加失败，部门不存在");
      return ResultUtils.error("添加失败，部门不存在");
    }
    int result = materialsDao.insertSelective(materials);
    if (result == 0) {
      logger.info("添加材料失败");
      return ResultUtils.error("添加材料失败");
    }
    materials = materialsDao.selectByPrimaryKey(materials.getId());
    ServiceCenterBean bean =    serviceCenterDao.getServiceCenter(materials.getServiceKey());
    //当为上传文档时，文件名添加文件后缀
    if(StringUtils.isNotBlank(materials.getFilePath())){
      String materialsName = materials.getMaterialsName() + materials.getFilePath().substring(materials.getFilePath().lastIndexOf("."),materials.getFilePath().length());
      materials.setMaterialsName(materialsName);
    }else{
      materials.setMaterialsName(materials.getMaterialsName());
    }
    materials.setServiceName(bean.getServiceName());
    logger.info("添加材料成功");
    return ResultUtils.ok("添加材料成功", materials);
  }

  /**
   * @param [materialsName]
   * @param serviceKey
   * @param deptId
   * @param materialsType @return int
   * @param id
   * @description: 校验名称
   * @author quboka
   * @date 2018/8/23 14:39
   */
  @Override
  public int checkoutByName(String serviceKey, Integer deptId, Integer materialsType,
      String materialsName, Integer id) {
    return materialsDao.checkoutByName(serviceKey, deptId, materialsType, materialsName, id);
  }

  //根据名称修改
  @Override
  public Map<String, Object> updateMaterialsByName(Materials materials) {
    logger.info("修改材料参数：" + ToStringBuilder.reflectionToString(materials));
    int result = materialsDao.updateMaterialsByName(materials);
    if (result == 0) {
      return ResultUtils.error("覆盖材料失败");
    }
    materials = materialsDao.selectByName(materials);
    //当为上传文档时，文件名添加文件后缀
    if(StringUtils.isNotBlank(materials.getFilePath())){
      String materialsName = materials.getMaterialsName() + materials.getFilePath().substring(materials.getFilePath().lastIndexOf("."),materials.getFilePath().length());
      materials.setMaterialsName(materialsName);
    }else{
      materials.setMaterialsName(materials.getMaterialsName());
    }
    return ResultUtils.ok("覆盖材料成功", materials);
  }


  /**
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加材料与业务的关联
   * @author quboka    HashMap<String, Object> paramsMap,
   *       String[] ids
   * @date 2018/8/28 10:04
   */
  @Override
  public Map<String, Object> addRelevanceBusinessType(MaterialsVo materialsVo) {
    materialsDao.deleteRel(materialsVo.getMaterialId());
    if (StringUtils.isBlank(materialsVo.getTypesId())) {
      return ResultUtils.ok("没有关联业务数据");
    }
    String[] typeIds = materialsVo.getTypesId().split(",");
    Materials materials = materialsDao.getMaterialsById(materialsVo.getMaterialId());
    if(MaterialsType.IS_FORM.getValue().equals(materials.getMaterialsType())){
      //申请单仅能上传一个文件，所以当材料为申请单时，
      List<String> businessNameList=materialsDao.checkIsfromMoreOne(typeIds);
      if(businessNameList.size()>0){
        return ResultUtils.error("业务："+businessNameList+"，已存在申请单，请勿重复添加！");
      }
      }

    int result = materialsDao.addRelevanceBusinessType(materialsVo);
    if (result == 0) {
      logger.info("关联业务失败");
      return ResultUtils.error("关联业务失败");
    }
    return ResultUtils.ok("关联业务成功");
  }

    @Override
  public Map<String, Object> updateMaterials(Integer afreshUpload, Materials materials,
      String typesId, MultipartFile file, HttpServletRequest request) throws IOException {
    logger.info("修改材料参数：" + ToStringBuilder.reflectionToString(materials));
    String filePath = null;
    //1上传 0不上传
    if (afreshUpload == 1) {
      if (file == null || file.isEmpty()) {
        logger.info("上传材料失败，请选择上传文件");
        return ResultUtils.error("请选择上传文件");
      }
      String originalFilename = file.getOriginalFilename();
      String fileName = FilenameUtils.removeExtension(originalFilename);

      //校验名称
      int checkoutResult = materialsDao
          .checkoutByName(materials.getServiceKey(), materials.getDeptId(),
              materials.getMaterialsType(), fileName, materials.getId());
      if (checkoutResult > 0) {
        logger.info("修改材料失败，名称重复");
        return ResultUtils.error("修改材料失败，名称重复");
      }
      String suffix = FilenameUtils.getExtension(originalFilename);
      boolean isWord = false;
      //判断后缀
      List<String> list = CommonConstant.suffixList;
      for (String extension : list) {
        if (suffix.equals(extension)) {
          isWord = true;
          break;
        }
      }
      if (!isWord) {
        logger.info("文件格式不正确，请重新上传");
        return ResultUtils.error("文件格式不正确，请重新上传");
      }
      //修改上传方式
      String path = FileUploadUtil.uploadFile(tempPath, file, suffix);
      File uploadFile = new File(path);
      StorageResVo storageResVo = storageUploadHelper.storageUpload(uploadFile, Integer.valueOf(
          FileType.WORD.getValue()));
      if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
        logger.info("存储网关返回文件路径为空");
        ResultUtils.error("存储网关返回文件路径为空");
      }
      if (uploadFile.exists()) {
        uploadFile.delete();
      }
      //文件绝对路径
      logger.info("文件绝对路径：" + storageResVo.getFileUrl());
      materials.setMaterialsName(fileName);
      filePath = storageResVo.getFileUrl();
      materials.setMaterialsName(fileName);
      materials.setFilePath(filePath);

    } else {
      //校验名称
      int checkoutResult = materialsDao
          .checkoutByName(materials.getServiceKey(), materials.getDeptId(),
              materials.getMaterialsType(),materials.getMaterialsName(), materials.getId());
      if (checkoutResult > 0) {
        logger.info("修改材料失败，名称重复");
        return ResultUtils.error("修改材料失败，名称重复");
      }
    }
    int result = materialsDao.updateMaterials(materials);
    if (result == 0) {
      logger.info("修改材料失败");
      return ResultUtils.error("修改材料失败");
    }

    if(MaterialsType.IS_FORM.getValue().equals(materials.getMaterialsType())){
      List<Integer> ids = materialsDao.getBusinessDetailIdsByMaterialsId(materials.getId());
      //当业务删除关联关系时，
      if(ids.size() > 0){
        businessTypeDao.updateBusinessTypeFormIsForm(BusinessTypeIsFormEnum.No.getValue(),ids);
      }
    }
    materialsDao.deleteRel(materials.getId());
    if(MaterialsType.IS_FORM.getValue().equals(materials.getMaterialsType())){
      //申请单仅能上传一个文件，所以当材料为申请单时，
      List<String> businessNameList=materialsDao.checkIsfromMoreOne(typesId.split(","));
      if(businessNameList.size()>0){
        return ResultUtils.error("业务："+businessNameList+"，已存在申请单，请勿重复添加！");
      }
    }
    if (StringUtils.isNotBlank(typesId)) {
      String[] typeIds = typesId.split(",");
      MaterialsVo materialsVo = new MaterialsVo();
      materialsVo.setMaterialId(materials.getId());
      materialsVo.setTypeIds(typeIds);
      materialsDao.addRelevanceBusinessType(materialsVo);
      //当关联材料为申请单时，业务更新为需要申请单
      if(MaterialsType.IS_FORM.getValue().equals(materials.getMaterialsType())){
        if(typeIds.length > 0){
          List<Integer> ids = new ArrayList<>(typeIds.length);
          for(String id:typeIds){
            ids.add(Integer.valueOf(id));
          }
          businessTypeDao.updateBusinessTypeFormIsForm(BusinessTypeIsFormEnum.Yes.getValue(),ids);
        }
      }
      }

    logger.info("修改材料成功");
    return ResultUtils.ok("修改材料成功");
  }

  /**
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询材料
   * @author quboka
   * @date 2018/8/28 13:28
   */
  @Override
  public Map<String, Object> getMaterialsList(MaterialsVo materialsVo) {
    List<Materials> list = null;
    Integer pageNum = materialsVo.getPageNum();
    Integer pageSize = materialsVo.getPageSize();

    SysUserBean userBean = ShiroUtils.getUserEntity();
    if (null == userBean) {
      return ResultUtils.error("当前用户未登录,请登陆后再次尝试！");
    }
    //0代表所有部门
    if (!userBean.getDeptId().equals(Integer.valueOf(CommonConstant.SUPER_ADMIN))) {
      Integer deptId = userBean.getDeptId();
      materialsVo.setDeptId(deptId);
    }

    //1.当为admin账号时,拿所有业务数据
    if(SysUserTypeEnum.Admin.getValue().equals(userBean.getType())){
      if (pageNum != -1) {
        PageHelper.startPage(pageNum, pageSize);
      }
      list = materialsDao.selectMaterialsList(materialsVo);
    }
    //2.当为统筹管理员时，显示该账号及子中心的所有数据
    if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())){
      //页面带查询serviceKey
         if(StringUtil.isNotEmpty(materialsVo.getServiceKey())){
          if (pageNum != -1) {
            PageHelper.startPage(pageNum, pageSize);
          }
           list = materialsDao.selectMaterialsList(materialsVo);
         } else {
           String parentKey = userBean.getServiceKey();
           List<String> serviceKeys = new ArrayList<>();
           serviceKeys.add(userBean.getServiceKey());
           List<ServiceCenterBean> centerBeans = serviceCenterDao
               .getServiceCenterByParentKey(parentKey);
           for (ServiceCenterBean bean : centerBeans) {
             serviceKeys.add(bean.getServiceKey());
           }
           materialsVo.setServiceKey(null);
           materialsVo.setServiceKeys(serviceKeys);
           if (pageNum != -1) {
             PageHelper.startPage(pageNum, pageSize);
           }
           list = materialsDao.selectMaterialsList(materialsVo);
           //取得统筹中心下的所有审批中心下所有业务
         }
    }

    //3.当为审批管理员时，统筹和admin管理员所建当业务类别只能查看
    if(SysUserTypeEnum.AuditCenterAdmin.getValue().toString().equals(userBean.getType())){
      //页面带查询serviceKey
      if(StringUtil.isNotEmpty(materialsVo.getServiceKey())){
        if (pageNum != -1) {
          PageHelper.startPage(pageNum, pageSize);
        }
        list = materialsDao.selectMaterialsList(materialsVo);
      }else{
        //取得审批管理员对于统筹中心的servicekey
        List<String> serviceKeys = new ArrayList<>();
        ServiceCenterBean bean =serviceCenterDao.getServiceCenter(userBean.getServiceKey());
        serviceKeys.add(bean.getParentKey());
        serviceKeys.add(userBean.getServiceKey());
        materialsVo.setServiceKey(null);
        materialsVo.setServiceKeys(serviceKeys);
        if (pageNum != -1) {
          PageHelper.startPage(pageNum, pageSize);
        }
        list = materialsDao.selectMaterialsList(materialsVo);
        for(Materials mt:list){
          //审批中心对于父级别的业务类别不能修改
          if(bean.getParentKey().equals(mt.getServiceKey())){
            mt.setShow(false);
          }
        }
      }
    }
    PageInfo<Materials> pageInfo = new PageInfo(list);

    return ResultUtils.ok("查询材料成功", pageInfo);
  }

  /**
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String       ,       java.lang.Object>
   * @description: 查询材料
   * @author ljfan
   * @date 2019/01/04 13:28
   */
  @Override
  public Map<String, Object> getMaterialsById(Integer materialsId) {
    return ResultUtils.check("查询材料成功", materialsDao.getMaterialsById(materialsId));
  }


  /***
   * @description: 查询材料  把材料根据类型分组
   *
   * @author ljfan
   * @date 2018/11/16 13:27
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String , java.lang.Object>
   */
  @Override
  public Map<String, Object> getMaterialsListByNoPage(MaterialsVo materialsVo) {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Map<String, Object> typeMap = null;
    List<Map> typeList = null;
    List<Map> materialsList = new ArrayList<>();
    Map<String, Object> typeListMap = null;
    List<Materials> list = null;
    List<String> serviceKeys = new ArrayList<>();
    SysUserBean userBean = ShiroUtils.getUserEntity();
    //业务中心校验
    //1.当为admin账号时,拿所有业务数据
    if(SysUserTypeEnum.Admin.getValue().equals(userBean.getType())){
        //当支持统筹办理时
        if(BusinessTypeIsComprehensiveEnum.Yes.getValue().equals(materialsVo.getIsComprehensive())){
          materialsVo.setServiceKey(materialsVo.getServiceKey());
        }else{
          materialsVo.setServiceKey(null);
        }
    }
    //2.当为统筹管理员时，显示该账号及子中心的所有数据
    if(SysUserTypeEnum.WholeCenterAdmin.getValue().equals(userBean.getType())){

        //当支持统筹办理时
        if(BusinessTypeIsComprehensiveEnum.Yes.getValue().equals(materialsVo.getIsComprehensive())){
          materialsVo.setServiceKey(materialsVo.getServiceKey());
          materialsVo.setServiceKeys(null);
        }else{
          //取得统筹中心及审批中心下的材料
          //取得统筹中心下的所有审批中心下所有材料列表展示
          String parentKey = userBean.getServiceKey();
          List<ServiceCenterBean> centerBeans  = serviceCenterDao.getServiceCenterByParentKey(parentKey);
          for(ServiceCenterBean bean:centerBeans){
            serviceKeys.add(bean.getServiceKey());
          }
          serviceKeys.add(parentKey);
          materialsVo.setServiceKeys(serviceKeys);
          materialsVo.setServiceKey(null);
        }
      }
    //3.当为审批管理员时，统筹和admin管理员所建材料列表获取
    if(SysUserTypeEnum.AuditCenterAdmin.getValue().equals(userBean.getType())) {
        //查询列表：取得审批管理员对于统筹中心
        ServiceCenterBean bean = serviceCenterDao.getServiceCenter(userBean.getServiceKey());
        serviceKeys.add(bean.getParentKey());
        serviceKeys.add(userBean.getServiceKey());
        materialsVo.setServiceKeys(serviceKeys);
        materialsVo.setServiceKey(null);
    }
    list = materialsDao.selectMaterialsList(materialsVo);
    if (null == list || list.size() == 0) {
      return ResultUtils.error("未查询到材料");
    }
    // 材料类型列表
    for (MaterialsType type : MaterialsType.values()) {
      typeList = new ArrayList<>();

      //遍历材料类型
      for (Materials materials : list) {
        typeListMap = new HashMap<String, Object>();
        if (type.getValue().equals(materials.getMaterialsType())) {
          typeListMap.put(type.getName(), type.getValue());
          typeListMap.put("id", materials.getId());
          typeListMap.put("materialsName", materials.getMaterialsName());
        }
        if (typeListMap.size() > 0) {
          typeList.add(typeListMap);
        }

      }
      if (typeList.size() > 0) {
        typeMap = new HashMap<String, Object>();
        typeMap.put(type.getName(), type.getValue());
        typeMap.put(type.name(), typeList);
        materialsList.add(typeMap);
      }


    }
    returnMap.put("list", list);
    returnMap.put("materialsType", materialsList);
    returnMap.put("list", list);
    returnMap.put("materialsType", materialsList);
    return ResultUtils.ok("查询材料成功", returnMap);
  }

  /**
   * 根据业务类型id查询所需材料
   *
   * @param businessTypeId 业务类型id
   * @param session session对象
   * @return
   */
  @Override
  public Map<String, Object> getMaterials(MaterialsVo materialsVo) {
    List<Materials> materialsList = materialsDao
        .selectMaterialsByTypeIdAndMaterialsType(materialsVo.getBusinessTypeId(),
            materialsVo.getMaterialsType());
    return ResultUtils.ok("查询成功", materialsList);
  }

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料
   * @author quboka
   * @date 2018/8/31 10:33
   */
  @Override
  public Map<String, Object> deleteMaterials(Integer[] ids) {
    Integer checkBusiness = materialsDao.checkBusinessIsUsed(ids);
    if (checkBusiness > 0) {
      return ResultUtils.error("请先删除关联业务");
    }
    //删除关联
    materialsDao.deleteRelByArray(ids);
    int result = materialsDao.deleteByArray(ids);
    if (result == 0) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      logger.info("删除材料失败");
      return ResultUtils.error("删除材料失败");
    }
    logger.info("删除材料成功");
    return ResultUtils.ok("删除材料成功");
  }
  public Map<String, Object> getStamp(HttpServletRequest request){
    String ip = request.getServerName();
    Integer port = request.getServerPort();
    String contextPath = request.getContextPath();
    String stampPath = ContentApi.PROTOCOL + ip + ":" + port + contextPath + "/template/1.png";
    logger.info("stampPath:"+stampPath);
    return ResultUtils.check("",stampPath);
  }

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料
   * @author quboka
   * @date 2018/8/31 10:33
   */
  @Override
  public Map<String, Object> deleteMaterialsIds(String ids) {
    String[] idsList = ids.split(",");
    Integer[] listIds =new Integer[2];
    if(idsList.length > 0){
       for(int i=0;i<idsList.length; i++){
               listIds[i]=Integer.valueOf(idsList[i]);
       }
    }
    Integer checkBusiness = materialsDao.checkBusinessIsUsed(listIds);
    if (checkBusiness > 0) {
      return ResultUtils.error("请先删除关联业务");
    }
    //删除关联
    materialsDao.deleteRelByArray(listIds);
    int result = materialsDao.deleteByArray(listIds);
    if (result == 0) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      logger.info("删除材料失败");
      return ResultUtils.error("删除材料失败");
    }
    logger.info("删除材料成功");
    return ResultUtils.ok("删除材料成功");
  }

  @Override
  public Map<String, Object> getBusinessMaterialsByIdCard(String idCard) {
      //根据时间查询该身份证用户最新一次办理完成的业务相关的材料信息
      logger.info("根据办理人身份证获取业务材料类型及材料列表接口入参为：" + idCard);
      List<Materials> materialsList = materialsDao.getBusinessMaterialsByIdCard(idCard);
      logger.info("根据办理人身份证获取业务材料类型及材料列表结果为：" + JSON.toJSONString(materialsList));
      return ResultUtils.ok("查询材料信息成功。",materialsList);
    }

}
