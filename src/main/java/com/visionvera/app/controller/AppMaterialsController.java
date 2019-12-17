package com.visionvera.app.controller;

import com.visionvera.api.handler.constant.StorageApi;
import com.visionvera.app.entity.AppointmentMaterials;
import com.visionvera.app.service.AppMaterialsService;
import com.visionvera.common.utils.StorageUploadHelper;
import com.visionvera.remoteservice.bean.PhotoBean;
import com.visionvera.remoteservice.controller.MaterialsController;
import com.visionvera.remoteservice.pojo.StorageResVo;
import com.visionvera.remoteservice.util.FileUploadUtil;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by jlm on 2019-03-25 18:46
 */
@RestController
@RequestMapping("/app")
public class AppMaterialsController {

    private static Logger logger = LoggerFactory.getLogger(AppMaterialsController.class);

    @Resource
    private AppMaterialsService appMaterialsService;
    @Resource
    private StorageUploadHelper storageUploadHelper;
    @Value("${temp.save.path}")
    private String tempPath;

    /**
     * 上传材料
     * @param file
     * @param appointMentId
     * @param materialType
     * @param materilaName
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadAppointMentMaterial")
    public Map<String,Object> uploadMaterial(MultipartFile file,Integer appointMentId,Integer materialType,String materilaName,Integer materialsId) throws IOException {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        String path = FileUploadUtil.uploadFile(tempPath, file, suffix);
        File uploadFile = new File(path);
        StorageResVo storageResVo = storageUploadHelper.storageUpload(uploadFile, Integer.valueOf(StorageApi.FileType.IMG.getValue()));
        if (StringUtil.isEmpty(storageResVo.getFileUrl())) {
            logger.info("存储网关返回文件路径为空");
            ResultUtils.error("存储网关返回文件路径为空");
        }
        if (uploadFile.exists()) {
            uploadFile.delete();
        }
        AppointmentMaterials appointmentMaterials = new AppointmentMaterials(appointMentId,materilaName,materialType,storageResVo.getFileUrl(),storageResVo.getFileId(),materialsId);
        return appMaterialsService.uploadAppointMaterial(appointmentMaterials);
    }

    /**
     * 删除材料；
     * @param id
     * @return
     */
    @RequestMapping("/delAppointMaterial")
    public Map<String,Object> delAppointMaterial(Integer id){
        return appMaterialsService.delAppointMaterial(id);
    }

    /**
     * 查询已上传材料
     * @param appointMentId
     * @return
     */
    @RequestMapping("/getMaterials")
    public Map<String,Object> getMaterials(Integer appointMentId){
        return appMaterialsService.getMaterials(appointMentId);
    }
}
