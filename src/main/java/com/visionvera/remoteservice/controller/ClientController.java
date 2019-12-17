package com.visionvera.remoteservice.controller;

import com.visionvera.remoteservice.service.ClientService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * ClassName: ClientController
 *
 * @author quboka
 * @Description: 客户信息
 * @date 2018年4月10日
 */
@RestController
@RequestMapping("client")
public class ClientController {

  private static Logger logger = LoggerFactory.getLogger(ClientController.class);

  @Resource
  private ClientService clientService;

  /**
   * @return Map<String   ,   Object>
   * @Description: 查询民族
   * @author quboka
   * @date 2018年4月10日
   */
  @RequestMapping("getNationalList")
  public Map<String, Object> getNationalList() {
    return clientService.getNationalList();
  }

  //只需要加上下面这段即可，注意不能忘记注解
  @InitBinder
  public void initBinder(WebDataBinder binder, WebRequest request) {
    //转换日期
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // CustomDateEditor为自定义日期编辑器
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

}
