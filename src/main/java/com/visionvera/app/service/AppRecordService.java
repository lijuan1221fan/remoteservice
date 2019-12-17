package com.visionvera.app.service;

import java.text.ParseException;
import java.util.Map;

/**
 * @author jlm
 * @ClassName:
 * @Description:
 * @date 2019/3/18
 */
public interface AppRecordService {

  Map<String, Object> getNumber(String idCardNo, String serviceKey,String androidBusinessType) throws ParseException;

}
