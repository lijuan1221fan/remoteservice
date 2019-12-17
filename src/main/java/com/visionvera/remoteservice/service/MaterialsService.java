package com.visionvera.remoteservice.service;

import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.pojo.MaterialsVo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: quboka
 * @Date: 2018/8/23 13:39
 * @Description:
 */
public interface MaterialsService {

  /**
   * @param [materials]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加材料
   * @author quboka
   * @date 2018/8/23 13:41
   */
  Map<String, Object> addMaterials(Materials materials);

  /**
   * @param [materialsName]
   * @param serviceKey
   * @param deptId
   * @param materialsType @return int
   * @param id
   * @description: 校验名称
   * @author quboka
   * @date 2018/8/23 14:37
   */
  public int checkoutByName(String serviceKey, Integer deptId, Integer materialsType,
      String materialsName, Integer id);

  /**
   * @param [materials]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 根据名称修改
   * @author quboka
   * @date 2018/8/23 15:09
   */
  Map<String, Object> updateMaterialsByName(Materials materials);

  /**
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 添加材料与业务的关联
   * @author quboka
   * @date 2018/8/28 10:04
   */
  Map<String, Object> addRelevanceBusinessType(MaterialsVo materialsVo);

  /**
   * @param [paramsMap]
   * @param afreshUpload
   * @param file
   * @param request @return java.util.Map<java.lang.String,java.lang.Object>
   * @description: 修改材料
   * @author quboka
   * @date 2018/8/28 10:22
   */
  Map<String, Object> updateMaterials(Integer afreshUpload, Materials materials, String typesId,
      MultipartFile file, HttpServletRequest request) throws IOException;

  /***
   * @description: 查询材料
   *
   * @author quboka
   * @date 2018/8/28 13:27
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String , java.lang.Object>
   */
   Map<String, Object> getMaterialsList(MaterialsVo materialsVo);
  /***
   * @description: 查询材料
   *
   * @author ljfan
   * @date 2019/01/04 13:27
   * @param [MaterialsId]
   * @return java.util.Map<java.lang.String , java.lang.Object>
   */
  Map<String, Object> getMaterialsById(Integer materialsId);

  /***
   * @description: 查询材料  把材料根据类型分组
   *
   * @author ljfan
   * @date 2018/11/16 13:27
   * @param [paramsMap]
   * @return java.util.Map<java.lang.String , java.lang.Object>
   */
  Map<String, Object> getMaterialsListByNoPage(MaterialsVo materialsVo);

  /**
   * 根据业务类型id查询所需材料
   *
   * @param businessTypeId 业务类型id
   * @param session session对象
   * @return
   */
  Map<String, Object> getMaterials(MaterialsVo materialsVo);

  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料
   * @author quboka
   * @date 2018/8/31 10:33
   */
  Map<String, Object> deleteMaterials(Integer[] ids);
  /**
   * @param [ids]
   * @return java.util.Map<java.lang.String   ,   java.lang.Object>
   * @description: 删除材料列表
   * @author quboka
   * @date 2018/8/31 10:33
   */
  Map<String, Object> deleteMaterialsIds(String ids);
  /**
   * @param
   * @param
   * @return 根据部门返回部门公章
   * @auther ljfan
   * @date 2019-02-12
   */
  public Map<String, Object> getStamp(HttpServletRequest request);

  /**
   * 根据办理人身份证获取业务材料类型及材料列表接口入参为
   * @param idCard
   * @return
   */
  Map<String, Object> getBusinessMaterialsByIdCard(String idCard);
}
