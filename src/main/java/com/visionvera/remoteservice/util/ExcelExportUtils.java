package com.visionvera.remoteservice.util;

import com.visionvera.remoteservice.bean.PhotoBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:poi根据模板导出excel,根据excel坐标赋值,如(B1)
 *
 * @author list
 */
public class ExcelExportUtils {

  private static Logger logger = LoggerFactory.getLogger(ExcelExportUtils.class);

  /**
   * 功能:按模板向Excel中相应地方填充数据
   */
  public static Boolean writeData(String templateFilePath, String outPath,
      Map<String, Object> dataMap) {
    if (dataMap == null || dataMap.isEmpty()) {
      return false;
    }
    InputStream inputStream = null;
    OutputStream os = null;
    Workbook workbook = null;
    try {
      //String pathname = ExcelExportUtils.class.getResource("/").getPath() + templateFilePath;
      String pathname = templateFilePath;
      logger.info(":生成文件路径" + outPath);
      logger.info(":模版路径" + pathname);
      inputStream = new FileInputStream(new File(pathname));
      //读取模板
      workbook = WorkbookFactory.create(inputStream);
      //数据填充的sheet
      Sheet sheet = workbook.getSheetAt(0);
      for (Entry<String, Object> entry : dataMap.entrySet()) {
        String point = entry.getKey();
        Object data = entry.getValue();
        if (!point.equals("image")) {
          TempCell cell = getCell(point, data, sheet);
          //指定坐标赋值
          setCell(cell, sheet);
        }
      }
      if (dataMap.containsKey("image")) {
        PhotoBean image = (PhotoBean) dataMap.get("image");
        byte[] bytes = HttpUtils.sendGet(image.getIconPath());
        // 插入 PNG 图片至 Excel
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        Drawing drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(3);
        anchor.setRow1(14);
        // 插入图片
        double standardWidth = 150;
        double standardHeight = 150;
        // 计算单元格的长宽
        double cellWidth = sheet.getColumnWidthInPixels(8);
        double cellHeight = sheet.getRow(3).getHeightInPoints() / 60 * 96;
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize(standardWidth / cellWidth, standardHeight / cellHeight);
      }
      // 输出文件
      os = new FileOutputStream(outPath);
      workbook.write(os);
      //设置生成excel中公式自动计算
      sheet.setForceFormulaRecalculation(true);
    } catch (Exception e) {
      logger.error("模版生成失败", e);
      return true;
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (os != null) {
        try {
          os.flush();
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return false;
  }


  /**
   * 功能:获取单元格数据,样式(根据坐标:B3)
   */
  private static TempCell getCell(String point, Object data, Sheet sheet) {
    TempCell tempCell = new TempCell();

    //得到列   字母
    String lineStr = "";
    String reg = "[A-Z]+";
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(point);
    while (m.find()) {
      lineStr = m.group();
    }
    //将列字母转成列号  根据ascii转换
    char[] ch = lineStr.toCharArray();
    int column = 0;
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      int post = ch.length - i - 1;
      int r = (int) Math.pow(10, post);
      column = column + r * ((int) c - 65);
    }
    tempCell.setColumn(column);

    //得到行号
    reg = "[0-9]+";
    p = Pattern.compile(reg);
    m = p.matcher(point);
    while (m.find()) {
      tempCell.setRow((Integer.parseInt(m.group()) - 1));
    }

    //获取模板指定单元格样式,设置到tempCell(写列表数据的时候用)
    Row rowIn = sheet.getRow(tempCell.getRow());
    if (rowIn == null) {
      rowIn = sheet.createRow(tempCell.getRow());
    }
    Cell cellIn = rowIn.getCell(tempCell.getColumn());
    if (cellIn == null) {
      cellIn = rowIn.createCell(tempCell.getColumn());
    }
    tempCell.setCellStyle(cellIn.getCellStyle());
    tempCell.setData(data);
    return tempCell;
  }

  /**
   * 功能:给指定坐标单元格赋值
   */
  private static void setCell(TempCell tempCell, Sheet sheet) {
    Row rowIn = sheet.getRow(tempCell.getRow());
    if (rowIn == null) {
      copyRows(tempCell.getRow() - 1, tempCell.getRow() - 1, tempCell.getRow(), sheet);
      rowIn = sheet.getRow(tempCell.getRow());
    }
    Cell cellIn = rowIn.getCell(tempCell.getColumn());
    if (cellIn == null) {
      cellIn = rowIn.createCell(tempCell.getColumn());
    }
    //根据data类型给cell赋值
    if (tempCell.getData() instanceof String) {
      cellIn.setCellValue((String) tempCell.getData());
    } else if (tempCell.getData() instanceof Integer) {
      cellIn.setCellValue((int) tempCell.getData());
    } else if (tempCell.getData() instanceof Double) {
      cellIn.setCellValue((double) tempCell.getData());
    } else {
      cellIn.setCellValue((String) tempCell.getData());
    }
    //样式
    if (tempCell.getCellStyle() != null) {
      cellIn.setCellStyle(tempCell.getCellStyle());
    }
  }

  /**
   * 功能:copy rows
   */
  private static void copyRows(int startRow, int endRow, int pPosition, Sheet sheet) {
    int pStartRow = startRow - 1;
    int pEndRow = endRow - 1;
    int targetRowFrom;
    int targetRowTo;
    int columnCount;
    CellRangeAddress region = null;
    int i;
    int j;
    if (pStartRow == -1 || pEndRow == -1) {
      return;
    }
    // 拷贝合并的单元格
    for (i = 0; i < sheet.getNumMergedRegions(); i++) {
      region = sheet.getMergedRegion(i);
      if ((region.getFirstRow() >= pStartRow)
          && (region.getLastRow() <= pEndRow)) {
        targetRowFrom = region.getFirstRow() - pStartRow + pPosition;
        targetRowTo = region.getLastRow() - pStartRow + pPosition;
        CellRangeAddress newRegion = region.copy();
        newRegion.setFirstRow(targetRowFrom);
        newRegion.setFirstColumn(region.getFirstColumn());
        newRegion.setLastRow(targetRowTo);
        newRegion.setLastColumn(region.getLastColumn());
        sheet.addMergedRegion(newRegion);
      }
    }
    // 设置列宽
    for (i = pStartRow; i <= pEndRow; i++) {
      Row sourceRow = sheet.getRow(i);
      columnCount = sourceRow.getLastCellNum();
      if (sourceRow != null) {
        Row newRow = sheet.createRow(pPosition - pStartRow + i);
        newRow.setHeight(sourceRow.getHeight());
        for (j = 0; j < columnCount; j++) {
          Cell templateCell = sourceRow.getCell(j);
          if (templateCell != null) {
            Cell newCell = newRow.createCell(j);
            copyCell(templateCell, newCell);
          }
        }
      }
    }
  }

  /**
   * 功能:copy cell,不copy值
   */
  private static void copyCell(Cell srcCell, Cell distCell) {
    distCell.setCellStyle(srcCell.getCellStyle());
    if (srcCell.getCellComment() != null) {
      distCell.setCellComment(srcCell.getCellComment());
    }
    CellType cellTypeEnum = srcCell.getCellTypeEnum();
    distCell.setCellType(cellTypeEnum);
  }

  public static void main(String[] args) {
    String templateFilePath = "D://temp.xlsx";
    String outFilePath = "D://data.xlsx";
    Map<String, Object> dataMap = new HashMap<>();
    dataMap.put("F2", "姓名");
    dataMap.put("H2", "123123123123123");
    dataMap.put("F3", "18511121234");
    dataMap.put("F5", "10份");
    dataMap.put("C7", "小孩");
    dataMap.put("F7", "20180621");
    dataMap.put("G7", "小孩");
    dataMap.put("H7", "小孩");
    dataMap.put("F13", "测试地址");
    dataMap.put("F14", "测试地址");
    dataMap.put("F15", "测试地址");
    dataMap.put("F16", "测试地址");
    dataMap.put("E17", "萨芬的故事电饭锅水电费感受到萨芬的故事电饭锅水电费感受到萨芬的故事电");
    dataMap.put("E19", "被投靠户户主意见被投靠户户主意见被投靠户户主意见被投靠户户主意见意见");
    dataMap.put("E20", "现住址信息现住址信息现住址信息现住址信息现住址信息现住址信息现住址信");
    dataMap.put("H25", "2018年6月11日");
    dataMap.put("D26", "测试员");
    dataMap.put("H26", "测试派出所");
    dataMap.put("H28", "10份");
    dataMap.put("D28", "你好");
    dataMap.put("H30", "测试派出所");
    ExcelExportUtils.writeData(templateFilePath, outFilePath, dataMap);
  }

  /**
   * 描述:临时单元格数据
   */
  static class TempCell {

    private int row;
    private int column;
    private CellStyle cellStyle;
    private Object data;

    public int getColumn() {
      return column;
    }

    public void setColumn(int column) {
      this.column = column;
    }

    public int getRow() {
      return row;
    }

    public void setRow(int row) {
      this.row = row;
    }

    public CellStyle getCellStyle() {
      return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
      this.cellStyle = cellStyle;
    }

    public Object getData() {
      return data;
    }

    public void setData(Object data) {
      this.data = data;
    }

  }

}
