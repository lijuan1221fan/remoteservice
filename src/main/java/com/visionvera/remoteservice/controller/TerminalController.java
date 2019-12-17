package com.visionvera.remoteservice.controller;

import com.visionvera.common.annonation.SysLogAnno;
import com.visionvera.remoteservice.bean.TerminalBean;
import com.visionvera.remoteservice.service.TerminalService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TerminalController
 *
 * @author quboka
 * @Description: 终端
 * @date 2018年4月12日
 */
@RestController
@RequestMapping("terminal")
public class TerminalController {

  private static Logger logger = LoggerFactory.getLogger(TerminalController.class);

  @Resource
  private TerminalService terminalService;

  /**
   * @param pageNum
   * @param pageSize
   * @param terminalId 终端id
   * @param terminalName 终端名称
   * @param number 终端号码
   * @param serviceKey 所属服务中心key
   * @return Map<String   ,   Object>
   * @Description: 获取终端列表
   * @author quboka
   * @date 2018年4月11日
   */
  @RequiresPermissions("terminal:query")
  @RequestMapping("getTerminalList")
  public Map<String, Object> getTerminalList(
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize,
      @RequestParam(value = "terminalId", required = false) Integer terminalId,
      @RequestParam(value = "terminalName", required = false) String terminalName,
      @RequestParam(value = "number", required = false) String number,
      @RequestParam(value = "serviceKey", required = false) String serviceKey
  ) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("pageNum", pageNum);
    paramsMap.put("pageSize", pageSize);
    paramsMap.put("terminalId", terminalId);
    paramsMap.put("terminalName", terminalName);
    paramsMap.put("number", number);
    paramsMap.put("serviceKey", serviceKey);

    return terminalService.getTerminalListByMap(paramsMap);
  }

  /**
   * @param terminalIds 终端id 多个用逗号隔开
   * @param isCoerce 是否强制  0：否 1：强制
   * @return Map<String   ,   Object>
   * @Description: 删除终端
   * @author quboka
   * @date 2018年4月11日
   */
  @SysLogAnno("删除终端")
  @RequestMapping("deleteTerminal")
  public Map<String, Object> deleteTerminal(
      @RequestParam(value = "terminalIds", required = true) String terminalIds,
      @RequestParam(value = "isCoerce", required = true) Integer isCoerce) {
    Map<String, Object> paramsMap = new HashMap<String, Object>();
    paramsMap.put("terminalIds", terminalIds);
    paramsMap.put("isCoerce", isCoerce);
    return terminalService.deleteTerminal(paramsMap);
  }

  /**
   * @param terminal
   * @return Map<String   ,   Object>
   * @Description: 添加终端
   * @author quboka
   * @date 2018年4月12日
   */
  @SysLogAnno("添加终端")
  @RequestMapping("addTerminal")
  public Map<String, Object> addTerminal(TerminalBean terminal) {
    return terminalService.addTerminal(terminal);
  }

  /**
   * @param terminal
   * @return Map<String,Object>
   * @Description: 修改终端
   * @author quboka
   * @date 2018年4月12日
   */
  @SysLogAnno("修改终端")
  @RequestMapping("updateTerminal")
  public Map<String, Object> updateTerminal(TerminalBean terminal) {
    return terminalService.updateTerminal(terminal);
  }
}
