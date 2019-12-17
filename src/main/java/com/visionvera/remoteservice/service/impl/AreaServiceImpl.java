package com.visionvera.remoteservice.service.impl;

import com.visionvera.remoteservice.bean.AreaBean;
import com.visionvera.remoteservice.bean.ServiceCenterBean;
import com.visionvera.remoteservice.dao.AreaDao;
import com.visionvera.remoteservice.service.AreaService;
import com.visionvera.remoteservice.util.ResultUtils;
import com.visionvera.remoteservice.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements AreaService {

    @Resource
    private AreaDao areaDao;

    @Override
    public Map<String, Object> getArea(String id) {
        return ResultUtils.check(areaDao.getArea(id));
    }

    @Override
    public void getAreaName(ServiceCenterBean bean) {
        if (bean != null) {
            List<String> areaIdList = getAreaIdList(bean);
            List<AreaBean> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(areaIdList)) {
                list = areaDao.getAreaName(areaIdList);
            }
            bean.setAreaList(list);
        }
    }

    public List<String> getAreaIdList(ServiceCenterBean bean) {
        if (bean == null) {
            return null;
        }
        ArrayList<String> ids = new ArrayList<>();
        if (StringUtil.isNotEmpty(bean.getProvince())) {
            ids.add(bean.getProvince());
        }
        if (StringUtil.isNotEmpty(bean.getCity())) {
            ids.add(bean.getCity());
        }
        if (StringUtil.isNotEmpty(bean.getArea())) {
            ids.add(bean.getArea());
        }
        if (StringUtil.isNotEmpty(bean.getStreet())) {
            ids.add(bean.getStreet());
        }
        if (StringUtil.isNotEmpty(bean.getCommunity())) {
            ids.add(bean.getCommunity());
        }
        return ids;
    }

}
