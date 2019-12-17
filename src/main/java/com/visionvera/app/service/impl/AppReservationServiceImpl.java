package com.visionvera.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.visionvera.app.dao.AppointmentInfoDao;
import com.visionvera.app.entity.AppointmentInfo;
import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.app.pojo.AppParaVo;
import com.visionvera.app.pojo.AppReservationVo;
import com.visionvera.app.pojo.AppWindowParaVo;
import com.visionvera.app.pojo.ReservationBean;
import com.visionvera.app.service.AppReservationService;
import com.visionvera.common.enums.AppointMentStateEnum;
import com.visionvera.common.enums.IsLeafEnum;
import com.visionvera.common.enums.ReservationTimeEnum;
import com.visionvera.common.enums.ServiceCenterTypeEnum;
import com.visionvera.common.enums.StateEnum;
import com.visionvera.remoteservice.bean.BusinessTypeBean;
import com.visionvera.remoteservice.bean.Materials;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.bean.SysDeptBean;
import com.visionvera.remoteservice.bean.WindowBean;
import com.visionvera.remoteservice.constant.CommonConstant;
import com.visionvera.remoteservice.dao.BusinessTypeDao;
import com.visionvera.remoteservice.dao.ServiceCenterDao;
import com.visionvera.remoteservice.dao.SysDeptBeanDao;
import com.visionvera.remoteservice.dao.WindowDao;
import com.visionvera.remoteservice.util.DateUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @ClassName AppReservationServiceImpl
 *description 预约
 * @author ljfan
 * @date 2019/03/19
 *version
 */
@Service("appReservationService")
public class AppReservationServiceImpl implements AppReservationService {
  private static Logger logger = LoggerFactory.getLogger(AppReservationServiceImpl.class);
  @Resource
  private ServiceCenterDao serviceCenterDao;
  @Resource
  private SysDeptBeanDao sysDeptBeanDao;
  @Resource
  private BusinessTypeDao businessTypeDao;
  @Resource
  private AppointmentInfoDao appointmentInfoDao;
  @Resource
  private WindowDao windowDao;

  //取得所有的服务中心
  @Override
  public Map<String,Object> getServiceCenter(AppReservationVo appReservationVo) {
    Integer pageNum = appReservationVo.pageNum;
    Integer pageSize = appReservationVo.pageSize;
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    ServiceCenterBean bean = new ServiceCenterBean();
    bean.setType(Long.valueOf(ServiceCenterTypeEnum.SERVER.getType()));
    bean.setState(StateEnum.Effective.getValue());
    bean.setServiceName(appReservationVo.getServiceName());
    List<ServiceCenterBean> serviceCenterList = serviceCenterDao.queryList(bean);
    PageInfo<ServiceCenterBean> pageInfo = new PageInfo<ServiceCenterBean>(serviceCenterList);
    return ResultUtils.check("查询成功", pageInfo);
  }
  //取得所有的部门                                       
  @Override
  public List<SysDeptBean> getDeptList() {
    return sysDeptBeanDao.getSysDeptListNotInDelete();
  }
  //根据部门、中心取得业务类别
  @Override
  public Map<String,Object> getBusinessesTypeList(Integer deptId, String serviceKey) {
    //根据村中心serviceKey 取得审批中心
    ServiceCenterBean serviceCenterBean = serviceCenterDao.getServiceListByChildServiceKey(serviceKey);
    if(serviceCenterBean == null){
      return ResultUtils.error("查询失败，中心不存在");
    }
    //取得统筹与审批所建的业务
    List<String> serviceKeys = new ArrayList<>();
    serviceKeys.add(serviceCenterBean.getParentKey());
    serviceKeys.add(serviceCenterBean.getServiceKey());
    BusinessTypeBean businessTypeBean = new BusinessTypeBean();
    businessTypeBean.setDeptId(deptId);
    businessTypeBean.setServiceKeys(serviceKeys);
    businessTypeBean.setServiceKey(null);
    businessTypeBean.setIsLeaf(IsLeafEnum.No.getValue());
    businessTypeBean.setState(StateEnum.Effective.getValue());
    List<BusinessTypeBean> list =businessTypeDao.getBusinessType(businessTypeBean);
    return ResultUtils.check(list);
  }
  //根据业务类别id取得业务详情列表
  @Override
  public List<BusinessTypeBean> getBusinessTypeInfoList(Integer businessTypeId) {
    Map<String,Object>  map = new HashMap<>();
    map.put("parentId",businessTypeId);
    map.put("isLeaf", IsLeafEnum.Yes.getValue());
    return businessTypeDao.getBusinessInfo(map);
  }
  //预约列表时间设定
  private ReservationBean getAppointList(BusinessTypeBean bean,Date date){
    ReservationBean reservationBean = new ReservationBean();
    reservationBean.setMorningNumber(CommonConstant.zero);
    reservationBean.setAfternoonNumber(CommonConstant.zero);
    reservationBean.setDate(date);
    reservationBean.setMorningLimitNumber(bean.getMorningLimitNumber());
    reservationBean.setAfternoonLimitNumber(bean.getAfternoonLimitNumber());
    reservationBean.setMorningAppointmentNumber(bean.getMorningLimitNumber());
    reservationBean.setAfternoonAppointmentNumber(bean.getAfternoonLimitNumber());
    return reservationBean;
  }

  /**
   *1、根据业务详情id 在businessType表获取业务信息，业务预约限制号数 = 业务初始值*窗口数，
   *2、根据业务详情id 在appointmentInfo表中获取该业务所有的预约号
   *3、根据业务详情id 取得业务详情明细 预约限定号
   * @param appParaVo
   * @return
   */
  @Override
  public Map<String,Object> getBusinessDetailInfoById(AppParaVo appParaVo) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    List<String> serviceKeys = new ArrayList<>();
    BusinessTypeBean bean = businessTypeDao.getBusinessDetailById(appParaVo.getBusinessesDetailId());
    if(bean == null){
      return  ResultUtils.error("业务不存在");
    }
    /**
     * 根据村级服务中心serviceKey 取得审批中心，根据审批中心serviceKey以及业务部门id 及一窗综办部门id
     * 取得所有窗口
     */
    ServiceCenterBean centerBean = serviceCenterDao.getServiceCenter(appParaVo.getServiceKey());
    if(centerBean == null){
      return  ResultUtils.error("服务中心不存在");
    }
    serviceKeys.add(centerBean.getParentKey());
    List<Integer> deptIds = new ArrayList<>();
    deptIds.add(bean.getDeptId());
    deptIds.add(CommonConstant.SUPER_ADMIN_DEPTID_TYPE);
    AppWindowParaVo appWindowParaVo = new AppWindowParaVo();
    appWindowParaVo.setDeptIds(deptIds);
    appWindowParaVo.setServiceKeys(serviceKeys);
    List<WindowBean> windows= windowDao.getWindowByServiceKeysAndDeptIds(appWindowParaVo);
    if(windows.size() == 0){
       return ResultUtils.error("暂无办理窗口");
    }
    //预约号=业务初始值*窗口数
    Integer morningLimitNumber = bean.getMorningLimitNumber()*windows.size();
    Integer afterLimitNumber  = bean.getAfternoonLimitNumber()*windows.size();
    bean.setMorningLimitNumber(morningLimitNumber);
    bean.setAfternoonLimitNumber(afterLimitNumber);
    List<AppointmentInfo>  appointmentInfos= appointmentInfoDao.getAppointmentInfoListByBusinessDetailId(appParaVo.getBusinessesDetailId());

    //获取从明天开始的一周时间
    List<Date> dates = DateUtil.getTimeInterval(new Date());
    List<ReservationBean> reservationVos = new ArrayList<>();
    /**
     *  1、根据日期获取当日已预约的号
     *  2、根据开始时间，获取已预约的最大上午号，最大下午号，
     */
    for(Date date:dates){
          ReservationBean reservationBean = getAppointList(bean,date);
         for(AppointmentInfo info:appointmentInfos){
               //初始化可预约号
                String st = info.getStartTime().replace("-","").substring(0,8);
                //判断预约日期时间是否一致
               if(sdf.format(date).equals(st)){
                 Integer startTime = DateUtil.getTime(info.getStartTime());
                 //根据上午/下午设定已预约号
                 if(startTime.equals(ReservationTimeEnum.MorningStartTime.getValue())){
                   reservationBean.setMorningNumber(info.getMorningNumber());
                   //支持可预约的号
                   reservationBean.setMorningAppointmentNumber(reservationBean.getMorningLimitNumber()-info.getMorningNumber() < 0 ? 0 : reservationBean.getMorningLimitNumber()-info.getMorningNumber());
                   reservationBean.setDate(date);
                 }
                 if(startTime.equals(ReservationTimeEnum.AfterStartTime.getValue())){
                   reservationBean.setAfternoonNumber(info.getAfternoonNumber());
                   //支持可预约的号
                   reservationBean.setAfternoonAppointmentNumber(reservationBean.getAfternoonLimitNumber()-info.getAfternoonNumber() < 0 ? 0 : reservationBean.getAfternoonLimitNumber()-info.getAfternoonNumber());
                   reservationBean.setDate(date);
                 }
               }
         }
      //设定支持预约的日期
      getIsSupport(reservationBean,bean.getBusinessDay(),date);
      reservationVos.add(reservationBean);
    }
    Map<String,Object> map = new HashMap<>(2);
    map.put("businessTypeBean",bean);
    map.put("reservationVos",reservationVos);
    return ResultUtils.ok("查询成功", map);
  }

  @Override
  public Map<String, Object> saveAppointmentInfo(AppointmentInfo appointmentInfo) {
    //取得业务详情
    BusinessTypeBean bean = businessTypeDao.getBusinessDetailById(appointmentInfo.getBusinessType());
    if(bean == null){
        return ResultUtils.error("预约业务不存在！");
    }
    //校验该时段同业务已预约
    List<AppointmentInfo> infoChecks = appointmentInfoDao.checkNumber(appointmentInfo);
    for(AppointmentInfo infoCheck : infoChecks){
      if(infoCheck != null){
        Integer startTime = DateUtil.getTime(appointmentInfo.getStartTime());
        //根据上午/下午设定已预约号
        if(startTime.equals(ReservationTimeEnum.MorningStartTime.getValue())){
          if(infoCheck.getMorningNumber() != null){
            return ResultUtils.error("该时间段您已有预约业务");
          }
        }
        if(startTime.equals(ReservationTimeEnum.AfterStartTime.getValue())){
          if(infoCheck.getAfternoonNumber() != null){
            return ResultUtils.error("该时间段您已有预约业务");
          }
        }
      }
    }
    //取得当前预约到的最大号，判断是否达到限制号
    AppointmentInfo checkFull = new AppointmentInfo();
    checkFull.setStartTime(appointmentInfo.getStartTime());
    checkFull.setBusinessType(appointmentInfo.getBusinessType());
    AppointmentInfo info = appointmentInfoDao.checkFull(checkFull);
    if(info != null ){
      //当已存在预约号时，判断预约号是否已满
      if((info.getMorningNumber() !=null && info.getMorningNumber() >= bean.getMorningLimitNumber())||(info.getAfternoonNumber() !=null && info.getAfternoonNumber() >= bean.getAfternoonLimitNumber())){
        return ResultUtils.ok("预约号已满，请预约其他时间段");
      }
    }
    //预约时间端时间设定
    Integer time =DateUtil.getTime(appointmentInfo.getStartTime());
    if(time.equals(ReservationTimeEnum.MorningStartTime.getValue())){
      appointmentInfo.setMorningNumber(CommonConstant.one);
    }
    if(time.equals(ReservationTimeEnum.AfterStartTime.getValue())){
      appointmentInfo.setAfternoonNumber(CommonConstant.one);
    }
    appointmentInfo.setState(StateEnum.Effective.getValue());
    int num = appointmentInfoDao.insertSelective(appointmentInfo);
    if(num > 0){
      return ResultUtils.ok("预约成功");
    }else{
      return ResultUtils.ok("预约失败");
    }

  }
  @Override
  public Map<String,Object> reservationRecord(AppParaVo appParaVo){
    Integer pageNum = appParaVo.pageNum;
    Integer pageSize = appParaVo.pageSize;
    if (pageNum != -1) {
      PageHelper.startPage(pageNum, pageSize);
    }
    List<ReservationBean> beans = appointmentInfoDao.effectiveReservation(appParaVo);
    for(ReservationBean bean:beans){
      List<Materials> materials = appointmentInfoDao.getMaterialsByappointmentId(bean.getId());
      logger.info("待上传的申请材料数：materials.size() ="+materials.size());
      //初始化上传材料数
      bean.setMaterialsNumber(CommonConstant.zero);
      if(materials.size() > 0){
        //设置上传材料数
        bean.setMaterialsNumber(materials.size());
        //遍历业务材料
        for(Materials ms:materials){
          //取得已上传材料
          AppointmentMaterials appointmentMaterials = new AppointmentMaterials(bean.getId(),ms.getId());
           List<AppointmentMaterials> filePaths = appointmentInfoDao.getAppointmentMaterial(appointmentMaterials);
           ms.setFilePaths(filePaths);
        }
        bean.setMaterialsList(materials);
      }
    }
    PageInfo<ReservationBean> pageInfo = new PageInfo<>(beans);
    return ResultUtils.ok("查询成功",pageInfo);
  }
  /**
   * 取消预约
   */
  @Override
  public Map<String,Object> cancellation(Integer appointmentId){
     AppointmentInfo info = appointmentInfoDao.selectByPrimaryKey(appointmentId);
     if(info !=null&& AppointMentStateEnum.Effective.getValue().equals(info.getState())){
          AppointmentInfo bean = new AppointmentInfo();
          bean.setState(AppointMentStateEnum.Cancle.getValue());
          bean.setId(appointmentId);
          int num = appointmentInfoDao.updateByPrimaryKeySelective(bean);
          if(num > 0){
            return ResultUtils.ok("取消成功");
          }else{
            return ResultUtils.error("取消失败");
          }
     }
     return ResultUtils.error("数据不存在");
  }
  /**
   * 设定是否可预约
   */
  private void  getIsSupport(ReservationBean bean,String datesStr,Date day){
    boolean support = DateUtil.IsSupport(datesStr,day);
    if(support){
      bean.setSupport(true);
    }
  }
}
