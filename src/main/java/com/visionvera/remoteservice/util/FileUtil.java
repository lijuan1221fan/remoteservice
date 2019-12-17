package com.visionvera.remoteservice.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author YZK
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 将本地文件转换成base64字符串
     *
     * @param imagePath 图片路径
     * @return  base64字符串
     */
    public static String fileToString(String imagePath){
        if(imagePath == null){
            return null;
        }
        byte[] data = null;
        InputStream in = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imagePath);
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                in.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            // 返回Base64编码过的字节数组字符串
            String image = encoder.encode(Objects.requireNonNull(data));
            return image;
        }
    }

    //图片转为byte数组
    public static byte[] imageTobyte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new FileImageInputStream(new File(path));
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }finally {
            try{
                if(output == null){
                    output.close();
                }
                if(input == null){
                    input.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String getFileSha1(File file)
    {
        FileInputStream in = null;
        try
        {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];

            int len = 0;
            while ((len = in.read(buffer)) > 0)
            {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0)
            {
                for (int i = 0; i < length; i++)
                {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
        return null;
    }
}
