package com.visionvera.app.service.impl;

import com.visionvera.app.dao.AppointmentMaterialsDao;
import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.app.service.AppMaterialsService;
import com.visionvera.common.enums.BusinessInfoTypeEnum;
import com.visionvera.remoteservice.bean.BusinessInfo;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.constant.PictureConstant;
import com.visionvera.remoteservice.dao.BusinessInfoDao;
import com.visionvera.remoteservice.dao.PhotoBeanDao;
import com.visionvera.remoteservice.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jlm on 2019-03-25 18:49
 */
@Service("appMaterialService")
public class AppMaterialsServiceImpl implements AppMaterialsService {

    private final Logger logger = LoggerFactory.getLogger(AppMaterialsServiceImpl.class);

    @Resource
    AppointmentMaterialsDao appointmentMaterialsDao;
    @Resource
    BusinessInfoDao businessInfoDao;
    @Resource
    PhotoBeanDao photoBeanDao;

    @Override
    public Map<String, Object> uploadAppointMaterial(AppointmentMaterials appointmentMaterials) {
        int num = appointmentMaterialsDao.insertSelective(appointmentMaterials);
        if(num > 0){
            return ResultUtils.ok("上传成功",appointmentMaterials);
        }
        return ResultUtils.error("上传失败,请重试");
    }

    @Override
    public Map<String, Object> delAppointMaterial(Integer id) {
        int num = appointmentMaterialsDao.updateByPrimaryKey(id);
        if(num > 0){
            return ResultUtils.ok("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    @Override
    public Map<String, Object> getMaterials(Integer appointMentId) {
        List<AppointmentMaterials> materialsList = appointmentMaterialsDao.selectByAppointmenId(appointMentId);
        return ResultUtils.ok("获取材料成功",materialsList);
    }

    @Override
    public List<PhotoBean> getAppointmentMaterialsList(Integer businessId) {
        BusinessInfo businessInfo = businessInfoDao.selectByPrimaryKey(businessId);
        List<AppointmentMaterials> materialsList = new ArrayList<AppointmentMaterials>();
        //预约app上传的材料查询
        if(businessInfo.getType().equals(BusinessInfoTypeEnum.Appointment.getValue()) && businessInfo.getAppointmentId() != null){
            materialsList = appointmentMaterialsDao.selectByAppointmenId(businessInfo.getAppointmentId());
            logger.info("预约材料：" + materialsList.toString());
        }
        List<PhotoBean> photoBeanList = new ArrayList<>();
        if(materialsList.size() > 0){
            for (AppointmentMaterials materials : materialsList) {
                PhotoBean materialPhoto = new PhotoBean();
                materialPhoto.setPictureId(materials.getFileId());
                materialPhoto.setIconPath(materials.getFilePath());
                materialPhoto.setPictureType(PictureConstant.CERTIFICATE_PHOTO);
                materialPhoto.setMaterialsId(materials.getMaterialsId());
                materialPhoto.setBusinessId(businessId);
                //插入
                photoBeanDao.insertSelective(materialPhoto);
                photoBeanList.add(materialPhoto);
            }
            logger.info("返回前端预约图片数据：" + photoBeanList);
        }
        return photoBeanList;
    }
}
