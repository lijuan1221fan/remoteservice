package com.visionvera.remoteservice.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jlm
 * @ClassName:
 * @Description: 文件上传工具类
 * @date 2018/11/14
 */
public class FileUploadUtil {

  public static String uploadFile(String filepath, MultipartFile file, String suffix)throws IOException {

    File dir = new File(filepath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File serverFile = new File(
        dir.getAbsolutePath() + "/" + UUIDGenerator.getJavaUuid() + "." + suffix);
    BufferedOutputStream stream = null;
    try {
      stream = new BufferedOutputStream(new FileOutputStream(serverFile));
      stream.write(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (stream != null) {
        stream.close();
      }
      return serverFile.getPath();
    }
  }
  /**
   * 上传文件至指定路径
   */
  public static String uploadFile(String filepath,String downloadscupath, MultipartFile file) {
    try{
      File dir = new File(filepath);
      if (!dir.exists()) {
        dir.mkdirs();
      } else {
        //删除原有文件
        deleteDir(filepath);
      }
      File uploadFile = new File(
          dir.getAbsolutePath() + "/" + file.getOriginalFilename());
        BufferedOutputStream stream = null;
        try {
          stream = new BufferedOutputStream(new FileOutputStream(uploadFile));
          stream.write(file.getBytes());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          if (stream != null) {
            try {
              stream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }

      return   downloadscupath+file.getOriginalFilename();
    }catch (Exception e){
      e.printStackTrace();
    }
     return null;
  }
  public static void deleteDir(String path){
    File file = new File(path);
    //判断是否待删除目//判录是存否在
    if(file.exists()){
      //取得当前目录下所有文件和文件夹
      String[] content = file.list();
      for(String name : content){
        File temp = new File(path, name);
        //判断是否是目录
        if(temp.isDirectory()){
          //递归调用，删除目录里的内容
          deleteDir(temp.getAbsolutePath());
          //删除空目录
          temp.delete();
        }else{
          //直接删除文件
          if(!temp.delete()){
            System.err.println("Failed to delete " + name);
          }
        }
      }
    }
  }
  /**
   * MultipartFile 转 File
   * @param file
   * @throws Exception
   */
  public static File multipartFileToFile(MultipartFile file) throws Exception {
    File toFile = null;
    InputStream ins = null;
    ins = file.getInputStream();
    toFile = new File(file.getOriginalFilename());
    inputStreamToFile(ins, toFile);
    ins.close();
    return toFile;
  }

  /**
   * File 转 MultipartFile
   * @param file
   * @throws Exception
   */
  public static void fileToMultipartFile( File file ) throws Exception {
    FileInputStream fileInput = new FileInputStream(file);
    MultipartFile toMultipartFile = new MockMultipartFile("file",file.getName(),"text/plain", IOUtils.toByteArray(fileInput));
    toMultipartFile.getInputStream();

  }


  public static void inputStreamToFile(InputStream ins, File file) {
    try {
      OutputStream os = new FileOutputStream(file);
      int bytesRead = 0;
      byte[] buffer = new byte[8192];
      while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
      os.close();
      ins.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
