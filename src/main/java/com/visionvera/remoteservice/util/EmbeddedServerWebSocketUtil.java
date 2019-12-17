package com.visionvera.remoteservice.util;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedServerWebSocketUtil {

    private static Logger logger = LoggerFactory.getLogger(EmbeddedServerWebSocketUtil.class);

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            //判断文件目录是否存在
            if(!dir.exists()){
                dir.mkdirs();
            }
            file = new File(dir.getPath() + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}