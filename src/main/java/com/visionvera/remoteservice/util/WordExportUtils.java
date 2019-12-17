package com.visionvera.remoteservice.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过word模板生成新的word工具类
 *
 * @author lijintao
 */
public class WordExportUtils {

  private static Logger logger = LoggerFactory.getLogger(WordExportUtils.class);

  /**
   * 根据模板生成新word文档
   *
   * @param templateName 模板名称  模版统一存在resources下面
   * @param outputUrl 新文档存放地址
   * @param textMap 需要替换的信息集合
   * @return 成功返回true, 失败返回false
   */
  public static boolean changWord(String templateName, String outputUrl,
      Map<String, String> textMap) {
    String inputUrl = WordExportUtils.class.getResource("/").getPath() + templateName;
    logger.info("模版路径" + inputUrl);
    //模板转换默认成功
    boolean changeFlag = false;
    FileOutputStream stream = null;
    try {
      //获取docx解析对象
      XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
      //解析替换文本段落对象
      WordExportUtils.changeText(document, textMap);

      //生成新的word
      File file = new File(outputUrl);
      stream = new FileOutputStream(file);
      document.write(stream);
    } catch (IOException e) {
      logger.error("模版生成失败", e);
      changeFlag = true;
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return changeFlag;

  }

  /**
   * 替换段落文本
   *
   * @param document docx解析对象
   * @param textMap 需要替换的信息集合
   */
  public static void changeText(XWPFDocument document, Map<String, String> textMap) {
    //获取段落集合
    List<XWPFParagraph> paragraphs = document.getParagraphs();

    for (XWPFParagraph paragraph : paragraphs) {
      //判断此段落时候需要进行替换
      List<XWPFRun> runs = paragraph.getRuns();
      for (XWPFRun run : runs) {
        //替换模板原来位置
        run.setText(changeValue(run.toString(), textMap), 0);
      }

    }

  }

  /**
   * 匹配传入信息集合与模板
   *
   * @param value 模板需要替换的区域
   * @param textMap 传入信息集合
   * @return 模板需要替换区域信息集合对应值
   */
  public static String changeValue(String value, Map<String, String> textMap) {
    Set<Map.Entry<String, String>> entries = textMap.entrySet();
    for (Map.Entry<String, String> entry : entries) {
      String key = entry.getKey();
      if (value == null) {
        continue;
      }
      if (value.indexOf(key) != -1) {
        value = entry.getValue();
      }
    }
    return value;
  }

  public static void main(String[] args) {
    //新生产的模板文件
    String outputUrl = "D:\\Users\\Administrator\\Desktop\\002.docx";
    System.out.println(WordExportUtils.class.getResource("/").getPath());
    Map<String, String> testMap = new HashMap<String, String>();
    testMap.put("applicantName", "小明");
    testMap.put("materialNum", "2");
    testMap.put("affiliation", "软件园");
    testMap.put("date", "2018年06月22日");

    WordExportUtils.changWord("template/template.docx", outputUrl, testMap);
  }
}