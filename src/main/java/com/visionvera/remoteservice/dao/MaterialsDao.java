package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.bean.TypeMaterialsRel;
import com.visionvera.remoteservice.pojo.MaterialsVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialsDao {

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  int deleteByPrimaryKey(Integer id);

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  int insert(Materials record);

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  int insertSelective(Materials record);

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  Materials selectByPrimaryKey(@Param("id") Integer id);

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  int updateByPrimaryKeySelective(Materials record);

  /**
   * @mbg.generated Wed Aug 22 17:37:30 CST 2018
   */
  int updateByPrimaryKey(Materials record);

  /**
   * @param [materialsName]
   * @param serviceKey
   * @param deptId
   * @param materialsType @return int
   * @description: 校验名称
   * @author quboka
   * @date 2018/8/23 14:40
   */
  int checkoutByName(@Param("serviceKey") String serviceKey, @Param("deptId") Integer deptId,
      @Param("materialsType") Integer materialsType, @Param("materialsName") String materialsName,
      @Param("id") Integer id);

  /**
   * @param [materials]
   * @return int
   * @description: 根据名称修改
   * @author quboka
   * @date 2018/8/23 15:22
   */
  int updateMaterialsByName(Materials materials);

  /**
   * @param [materialsName]
   * @return com.visionvera.remoteservice.bean.Materials
   * @description: 根据名称修改文件路径
   * @author quboka
   * @date 2018/8/23 15:22
   */
  Materials selectByName(Materials materials);

  /**
   * @param [paramsMap]
   * @return int
   * @description: 添加材料与业务的关联
   * @author quboka
   * @date 2018/8/28 10:11
   */
  int addRelevanceBusinessType(MaterialsVo materialsVo);

  /**
   * @param [paramsMap]
   * @return int
   * @description: 校验材料与业务的关联是否已存在
   * @author ljfan
   * @date 2018/11/9 10:11
   */
  List<TypeMaterialsRel> checkRelevanceBusinessType(MaterialsVo materialsVo);

  /***
   * @description:修改材料
   *
   * @author quboka
   * @date 2018/8/28 11:24
   * @param [materials]
   * @return int
   */
  int updateMaterials(Materials materials);

  /**
   * @param [id]
   * @return void
   * @description: 删除关联
   * @author quboka
   * @date 2018/8/28 12:15
   */
  void deleteRel(Integer materialsId);

  /***
   * @description: 查询材料
   *
   * @author quboka
   * @date 2018/8/28 13:32
   * @param [paramsMap]
   * @return java.util.List<com.visionvera.remoteservice.bean.Materials>
   */
  List<Materials> selectMaterialsList(MaterialsVo materialsVo);
  /***
   * @description: 查询材料
   *
   * @author ljfan
   * @date 2019/01/04 13:32
   * @param [materialsId]
   * @return Materials
   */
  Materials getMaterialsById(@Param("materialsId") Integer materialsId);

  List<Materials> selectMaterialsByTypeId(Integer businessTypeId);

  /**
   * @param [ids]
   * @return void
   * @description: 删除多个材料业务关联
   * @author quboka
   * @date 2018/8/31 10:41
   */
  void deleteRelByArray(Integer[] ids);

  /**
   * @param [ids]
   * @return int
   * @description: 删除多个材料
   * @author quboka
   * @date 2018/8/31 10:48
   */
  int deleteByArray(Integer[] ids);

  /**
   * @param [deptId]
   * @return void
   * @description: 根据部门删除材料
   * @author quboka
   * @date 2018/8/31 14:40
   */
  void deleteByDeptId(Integer[] deptId);

  /**
   * @param [ids]
   * @return java.util.List<java.lang.Integer>
   * @description: 根据部门查询 材料id
   * @author quboka
   * @date 2018/8/31 14:47
   */
  List<Integer> selectIdByDeptId(Integer[] ids);

  List<Integer> selectIdListByDeptId(String[] ids, Integer deptId);

  List<Materials> selectMaterialsByTypeIdAndMaterialsType(
      @Param("businessTypeId") Integer businessTypeId,
      @Param("materialsType") Integer materialsType);


  List<Materials> selectByIds(String[] split);

  Integer checkBusinessIsUsed(Integer[] ids);

  Integer getCountNotInDeptByMaterialId(@Param("materialsIdArray") String[] materialsIdArray,
      @Param("depId") Integer depId);


  /**
   * 根据业务列表ID 查询已关联材料
   * @param materialsVo
   * @return
   */
  List<Materials> checkRelevanceBusinessTypeByBusinessDeatilId(MaterialsVo materialsVo);
  List<String> checkIsfromMoreOne(@Param("typeIds")String[] typeIds);

  /**
   * 根据办理人身份证获取业务材料类型及材料列表接口入参为
   * @param idCard
   * @return
   */
  List<Materials> getBusinessMaterialsByIdCard(String idCard);
  /**
   * 根据材料ID 查询业务ID
   */
  List<Integer> getBusinessDetailIdsByMaterialsId(Integer id);
}
