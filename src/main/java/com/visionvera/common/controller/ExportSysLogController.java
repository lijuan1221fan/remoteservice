package com.visionvera.common.controller;

import com.visionvera.common.aspect.SysLogAspect;
import com.visionvera.common.entity.SysLog;
import com.visionvera.common.utils.FileUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jlm
 * @ClassName:
 * @Description: 导出系统日志Excel
 * @date 2018/10/12
 */
@RestController
@RequestMapping("/export")
public class ExportSysLogController {

  @Resource
  private SysLogAspect sysLogAspect;

  @RequiresPermissions("log:export")
  @RequestMapping(value = "/xlog")
  public void exportXls(HttpServletResponse response, HttpServletRequest request,
      @RequestParam(value = "userName", required = false) String userName,
      @RequestParam(value = "operation", required = false) String operation,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "endTime", required = false) String endTime)
      throws IOException, ParseException {

    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("userName", userName);
    paramMap.put("operation", operation);
    paramMap.put("startTime", startTime);
    paramMap.put("endTime", endTime);

    // 查询符合条件所有日志信息
    List<SysLog> logList = sysLogAspect.queryAllSysLog(paramMap);

    // 生成Excel文件
    HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    HSSFSheet sheet = hssfWorkbook.createSheet("日志数据");
    // 表头
    HSSFRow headRow = sheet.createRow(0);
    headRow.createCell(0).setCellValue("编号");
    headRow.createCell(1).setCellValue("用户名");
    headRow.createCell(2).setCellValue("用户操作");
    headRow.createCell(3).setCellValue("执行时间");
    headRow.createCell(4).setCellValue("执行方法");
      headRow.createCell(5).setCellValue("用户操作时间");
    // 表格数据
    for (SysLog sysLog : logList) {
      HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
      dataRow.createCell(0).setCellValue(sysLog.getId());
      dataRow.createCell(1).setCellValue(sysLog.getUsername());
      dataRow.createCell(2).setCellValue(sysLog.getOperation());
      dataRow.createCell(3).setCellValue(sysLog.getTime());
      dataRow.createCell(4).setCellValue(sysLog.getMethod());
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = fmt.parse(sysLog.getcTime().toString());
      String ctime = fmt.format(date);
        dataRow.createCell(5).setCellValue(ctime);
    }

    // 下载导出
    // 设置头信息
    response.setContentType(
        "application/vnd.ms-excel");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(new Date());
    String filename = "系统日志" + date + ".xls";
    String agent = request.getHeader("user-agent");
    filename = FileUtils.encodeDownloadFilename(filename, agent);
    response.setHeader("Content-Disposition",
        "attachment;filename=" + filename);

    ServletOutputStream outputStream = response.getOutputStream();
    hssfWorkbook.write(outputStream);
    // 关闭
    hssfWorkbook.close();

  }
}
