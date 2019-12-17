package com.visionvera.remoteservice.dao;

import com.visionvera.remoteservice.bean.AreaBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaDao {

    List<AreaBean> getArea(@Param(value = "id") String id);

    List<AreaBean> getAreaName(@Param("idList") List<String> idList);
}
