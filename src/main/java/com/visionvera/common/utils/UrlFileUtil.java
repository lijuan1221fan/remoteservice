package com.visionvera.common.utils;

import com.visionvera.remoteservice.exception.MyException;
import com.visionvera.remoteservice.util.ResultUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;


/**
 * @author jlm
 * @ClassName: UrlFileUtil
 * @Description: 图片工具类
 * @date 2018/10/23
 */
public class UrlFileUtil {

  /**
   * @param acceptUrl 从网关获取图片url
   * @param filename 文件名,防止重复建议使用时间戳
   * @param savePath 图片保存到服务器路径
   * @Description:
   * @Date: 2018/10/23
   * @return: void
   */
  public static void download(String acceptUrl, String filename, String savePath) {

    // 构造URL
    URL url = null;
    try {
      url = new URL(acceptUrl);
      // 打开连接
      URLConnection con = url.openConnection();
      //设置请求超时为5s
      con.setConnectTimeout(5 * 1000);
      // 输入流
      InputStream is = con.getInputStream();

      // 1K的数据缓冲
      byte[] bs = new byte[1024];
      // 读取到的数据长度
      int len;
      // 输出的文件流
      File sf = new File(savePath);
      if (!sf.exists()) {
        sf.mkdirs();
      }
      OutputStream os = new FileOutputStream(sf.getPath() + "/" + filename + ".jpg");
      // 开始读取
      while ((len = is.read(bs)) != -1) {
        os.write(bs, 0, len);
      }
      // 完毕，关闭所有链接
      os.close();
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static Map<String, Object> fileToZip(String sourceFilePath, String zipFilePath,
      String fileName) throws UnsupportedEncodingException {
    boolean flag = false;
    File sourceFile = new File(sourceFilePath);
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    FileOutputStream fos = null;
    ZipOutputStream zos = null;
    if (sourceFile.exists() == false) {
      return ResultUtils.error("待压缩的文件目录：" + sourceFilePath + "不存在.");
    } else {
      try {
        File zipFile = new File(zipFilePath + "/" + fileName);
        if (zipFile.exists()) {
          return ResultUtils.ok("exists", "exists");
        } else {
          File[] sourceFiles = sourceFile.listFiles();
          if (null == sourceFiles || sourceFiles.length < 1) {
            return ResultUtils.error(zipFilePath + "待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
          } else {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] bufs = new byte[1024 * 10];
            for (int i = 0; i < sourceFiles.length; i++) {
              //创建ZIP实体，并添加进压缩包
              ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
              zos.putNextEntry(zipEntry);
              //读取待压缩的文件并写进压缩包里
              fis = new FileInputStream(sourceFiles[i]);
              bis = new BufferedInputStream(fis, 1024 * 10);
              int read = 0;
              while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                zos.write(bufs, 0, read);
              }
              fis.close();
              bis.close();
            }
            flag = true;
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      } finally {
        //关闭流
        try {
          if (null != bis) {
            bis.close();
          }
          if (null != zos) {
            zos.close();
          }
          if (null != fis) {
            fis.close();
          }
          if (null != fos) {
            fos.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
    return ResultUtils.ok("压缩文件成功");
  }

  /**
   * 业务结束时删除数据
   *
   * @param path
   * @return
   */
  public static boolean delAllFile(String path) {
    boolean flag = false;
    File file = new File(path);
    if (!file.exists()) {
      return flag;
    }
    if (!file.isDirectory()) {
      return flag;
    }
    String[] tempList = file.list();
    File temp = null;
    for (int i = 0; i < tempList.length; i++) {
      if (path.endsWith(File.separator)) {
        temp = new File(path + tempList[i]);
      } else {
        temp = new File(path + File.separator + tempList[i]);
      }
      if (temp.isFile()) {
        temp.delete();
      }
      if (temp.isDirectory()) {
        //先删除文件夹里面的文件
        delAllFile(path + "/" + tempList[i]);
        flag = true;
      }
    }
    return flag;
  }

  public static String downloadFile(String acceptUrl, String savePath) {

    // 构造URL
    URL url = null;
    try {
      url = new URL(acceptUrl);
      // 打开连接
      URLConnection con = url.openConnection();
      //设置请求超时为5s
      con.setConnectTimeout(5 * 1000);
      // 输入流
      InputStream is = con.getInputStream();

      // 1K的数据缓冲
      byte[] bs = new byte[1024];
      // 读取到的数据长度
      int len;
      // 输出的文件流
      File sf = new File(savePath);
      if (!sf.exists()) {
        sf.mkdirs();
      }
      String fileName = FilenameUtils.getName(acceptUrl);
      String suffix = FilenameUtils.getExtension(acceptUrl);
      OutputStream os = new FileOutputStream(sf.getPath() + "/" + fileName);
      // 开始读取
      while ((len = is.read(bs)) != -1) {
        os.write(bs, 0, len);
      }
      // 完毕，关闭所有链接
      os.close();
      is.close();
      return sf.getAbsolutePath() + "/" + fileName;
    } catch (IOException e) {
      e.printStackTrace();
      throw new MyException("下载图片时出现错误，请重试");
    }
  }

  public static String downloadIMGFile(String acceptUrl, String savePath) {
    // 构造URL
    URL url = null;
    try {
      url = new URL(acceptUrl);
      // 打开连接
      URLConnection con = url.openConnection();
      //设置请求超时为5s
      con.setConnectTimeout(5 * 1000);
      // 输入流
      InputStream is = con.getInputStream();

      // 1K的数据缓冲
      byte[] bs = new byte[1024];
      // 读取到的数据长度
      int len;
      // 输出的文件流
      File sf = new File(savePath);
      if (!sf.exists()) {
        sf.mkdirs();
      }
      String fileName = UUID.randomUUID().toString().replaceAll("-", "");
      String suffix = FilenameUtils.getExtension(acceptUrl);
      OutputStream os = new FileOutputStream(sf.getPath() + "/" + fileName + "." + suffix);
      // 开始读取
      while ((len = is.read(bs)) != -1) {
        os.write(bs, 0, len);
      }
      // 完毕，关闭所有链接
      os.close();
      is.close();
      return sf.getAbsolutePath() + "/" + fileName + "." + suffix;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
