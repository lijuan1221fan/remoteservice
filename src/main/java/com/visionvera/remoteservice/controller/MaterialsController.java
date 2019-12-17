package com.visionvera.remoteservice.controller;


import com.visionvera.api.handler.constant.StorageApi.FileType;
import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.common.validator.group.CheckGroup;
import com.visionvera.common.validator.group.MaterialsByParamterGroup;
import com.visionvera.common.validator.group.QueryGroup;
import com.visionvera.common.validator.group.QueryMaterialsGroup;
import com.visionvera.common.validator.util.AssertUtil;
import com.visionvera.common.validator.util.ValidateUtil;
import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.CommonConfigDao;
import com.visionvera.remoteservice.dao.StorageResDao;
import com.visionvera.remoteservice.pojo.MaterialsVo;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.service.MaterialsService;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Auther: quboka
 * @Date: 2018/8/22 17:42
 * @Description: 材料
 */
@RestController
@RequestMapping("/materials")
public class MaterialsController {

  private static Logger logger = LoggerFactory.getLogger(MaterialsController.class);

  @Autowired
  private MaterialsService materialsService;
  @Resource
  private CommonConfigDao commonConfigDao;
  @Resource
  private StorageResDao storageResDao;
  @Value("${temp.save.docpath}")
  private String tempPath;
  @Resource
  private StorageUploadHelper storageUploadHelper;

  /**
   * @description: 上传并保存材料
   * @author jlm
   * @date 2018/11/14
   * @param [ deptId 部门, materialsType 材料类别 , isUpload 1：上传 0：不上传, materialsName 材料名称,isCoverage
   * 1:覆盖 0：第一次, file, session, request]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   */
  @SysLogAnno("新建材料")
  @RequestMapping(value = "/uploadAndSave", method = RequestMethod.POST)
  public Map<String, Object> uploadAndSave(
      @RequestParam(value = "deptId", required = true) Integer deptId,
      @RequestParam(value = "materialsType", required = true) Integer materialsType,
      @RequestParam(value = "isUpload", required = true) Integer isUpload,
      @RequestParam(value = "isCoverage", required = true) Integer isCoverage,
      @RequestParam(value = "serviceKey", required = true) String serviceKey,
      String materialsName, MultipartFile file) throws IOException {
      AssertUtil.isBlank(serviceKey,"请选择中心");
      AssertUtil.isNull(deptId,"请选择部门");
      AssertUtil.isNull(materialsType,"请选择业务类型");
      String filePath = null;
    //1上传 0不上传
    if (isUpload == 1) {
      if (file == null || file.isEmpty()) {
        logger.info("上传材料失败，请选择上传文件");
        return ResultUtils.error("材料为空，请选择正确上传文件");
      }
      String originalFilename = file.getOriginalFilename();
      String fileName = FilenameUtils.removeExtension(originalFilename);
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
      StorageResVo storageResVo = storageUploadHelper
          .storageUpload(uploadFile, Integer.valueOf(FileType.WORD.getValue()));
      if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
        logger.info("存储网关返回文件路径为空");
        ResultUtils.error("存储网关返回文件路径为空");
      }
      if (uploadFile.exists()) {
        uploadFile.delete();
      }
      //文件绝对路径
      logger.info("文件绝对路径：" + storageResVo.getFileUrl());
      materialsName = fileName;
      filePath = storageResVo.getFileUrl();
    } else {
      if (StringUtils.isEmpty(materialsName)) {
        logger.info("添加材料失败,请添加材料名称");
        return ResultUtils.error("请添加材料名称");
      }
    }
    Map<String, Object> resultMap = null;
    Materials materials = new Materials(materialsName, serviceKey, deptId, materialsType, isUpload,
        filePath);
    if (isCoverage == 0) {
    resultMap = materialsService.addMaterials(materials);
    } else {
      //修改
      resultMap = materialsService.updateMaterialsByName(materials);
    }
    return resultMap;
  }

  /**
   * 判断材料是否已经存在
   *
   * @param deptId
   * @param materialsType
   * @param materialsName
   * @return
   * @author jlm
   */
  @RequestMapping(value = "/uploadAndCheck", method = RequestMethod.POST)
  public Map<String, Object> uploadAndCheck(@RequestBody MaterialsVo materialsVo) {
    ValidateUtil.validate(materialsVo, CheckGroup.class);
    int checkout = materialsService
        .checkoutByName(materialsVo.getServiceKey(), materialsVo.getDeptId(), materialsVo.getMaterialsType(),
            materialsVo.getMaterialsName(), null);
    if (checkout > 0) {
      return ResultUtils.error("材料存在");
    } else {
      return ResultUtils.ok("材料不存在");
    }
  }

  /**
   * @param [materialId, typesId]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加材料与业务的关联
   * @author quboka
   * @date 2018/8/28 9:59
   */
  @SysLogAnno("添加材料与业务的关联")
  @RequestMapping("relevanceBusinessType")
  public Map<String, Object> relevanceBusinessType(@RequestBody MaterialsVo materialsVo) {
    materialsVo.setTypeIds(materialsVo.getTypesId().split(","));
    return materialsService.addRelevanceBusinessType(materialsVo);
  }

  /**
   * @param [materials, typesId]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 修改材料
   * @author quboka
   * @date 2018/8/28 10:22
   */
  @SysLogAnno("修改材料")
  @RequestMapping("updateMaterials")
  public Map<String, Object> updateMaterials(
      @RequestParam(value = "typesId", required = false) String typesId,
      @RequestParam(value = "afreshUpload", defaultValue = "0") Integer afreshUpload,
      Materials materials, MultipartFile file, HttpServletRequest request
  ) throws IOException {
    return materialsService.updateMaterials(afreshUpload, materials, typesId, file, request);
  }

  /**
   * @param [pageNum, pageSize, id, materialsName, serviceKey, deptId, materialsType, isUpload]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询材料
   * @author quboka
   * @date 2018/8/28 13:27
   */
  @RequestMapping("getMaterialsList")
  public Map<String, Object> getMaterialsList(@RequestBody MaterialsVo materialsVo) {
    ValidateUtil.validate(materialsVo, QueryGroup.class);
    return materialsService.getMaterialsList(materialsVo);
  }

  /**
   * @param [materialsId]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询材料
   * @author ljfan
   * @date 2019/01/04 13:27
   */
  @RequestMapping("getMaterialsById")
  public Map<String, Object> getMaterialsById(@RequestParam(value = "materialsId", required =true)
      Integer materialsId) {
    AssertUtil.isEmpty(materialsId,"材料id不能为空");
    return materialsService.getMaterialsById(materialsId);
  }



  /**
   * @param serviceKey, deptId
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 查询根据部门id和service Key取得材料
   * @author ljfan
   * @date 201811/14 13:27
   */
  @PostMapping("getMaterialsListByDeptIdAndServiceKey")
  public Map<String, Object> getMaterialsListByDeptIdAndServiceKey(
      @RequestBody MaterialsVo materialsVo) {
    ValidateUtil.validate(materialsVo, MaterialsByParamterGroup.class);
    return materialsService.getMaterialsListByNoPage(materialsVo);
  }

  /**
   * 根据业务类型id查询所需申请材料
   *
   * @param businessTypeId 父id
   * @param materialsType 材料类型
   * @return
   */
  @RequestMapping("getMaterials")
  public Map<String, Object> getMaterials(@RequestBody MaterialsVo materialsVo) {
    ValidateUtil.validate(materialsVo, QueryMaterialsGroup.class);
    return materialsService.getMaterials(materialsVo);
  }


  /**
   * @param []
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料
   * @author quboka
   * @date 2018/8/31 10:13
   */
  @SysLogAnno("删除材料")
  @GetMapping("deleteMaterials")
  public Map<String, Object> deleteMaterials(Integer[] ids) {
    AssertUtil.isEmpty(ids, "待删除材料主键不能为空");
    return materialsService.deleteMaterials(ids);
  }


  /**
   * @param []
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料集合
   * @author quboka
   * @date 2018/8/31 10:13
   */
  @SysLogAnno("删除材料")
  @GetMapping("deleteMaterialsIds")
  public Map<String, Object> deleteMaterials(String ids) {
    AssertUtil.isEmpty(ids, "待删除材料主键不能为空");
    return materialsService.deleteMaterialsIds(ids);
  }
  /**
   * 获取公章
   *
   * @param
   * @return 根据部门返回部门公章
   * @auther ljfan
   * @date 2019-02-12
   */
  @RequestMapping(value = "getStamp", method = RequestMethod.GET)
  public Map<String,Object> getStamp(HttpServletRequest request){
    return materialsService.getStamp(request);
  }

  /**
   * 根据办理人身份证获取业务材料类型及材料列表
   * @param idCard
   * @return
   */
  @GetMapping("getBusinessMaterialsByIdCard")
  public Map<String, Object> getBusinessMaterialsByIdCard(String idCard,String token){
    ValidateUtil.validate(idCard);
    if(!token.equals("123456")){
      return ResultUtils.error("token错误");
    }
    return materialsService.getBusinessMaterialsByIdCard(idCard);
  }

}
