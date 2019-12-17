package com.visionvera.remoteservice.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterName;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijintao
 */
public class ImageUtil {

  private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

  public static void markByText(String logoText, String srcImagePath, String targetImagePath,
      Integer degree) {
    InputStream is = null;
    OutputStream os = null;

    try {
      Image srcImage = ImageIO.read(new File(srcImagePath));
      BufferedImage buffImg = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
          BufferedImage.TYPE_INT_BGR);
      Graphics2D g = buffImg.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(srcImage
              .getScaledInstance(srcImage.getWidth(null), srcImage.getHeight(null), Image.SCALE_SMOOTH),
          0, 0, null);
      if (degree != null) {
        g.rotate(Math.toRadians(degree), (double) buffImg.getWidth(null) / 2,
            (double) buffImg.getHeight(null) / 2);
      }
      g.setColor(Color.black);
      g.setFont(new Font("宋体", Font.BOLD, 20));
      float alpha = 0.5f;
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
      g.drawString(logoText, 260, 785);
      g.dispose();//消除Windows图形资源，避免多人使用时出现内存泄漏，及时使用
      os = new FileOutputStream(targetImagePath);
      ImageIO.write(buffImg, "JPG", os);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) {
          is.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        if (os != null) {
          os.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void markByIcon(String imgPath, String srcImagePath, String targetPath, int x,
      int y, int w, int h) throws IOException {
    ImageIcon imageIcon = new ImageIcon(imgPath);
    markByIcon(imageIcon.getImage(), srcImagePath, targetPath, x, y, w, h);
  }

  public static void markByIcon(Image img, String srcImagePath, String targetPath, int x, int y,
      int w, int h) throws IOException {
    OutputStream os = null;
    try {
      logger.info("图片水印开始添加。。。");
      logger.info("图片水印输入路径" + srcImagePath);
      logger.info("图片水印输出路径" + targetPath);
      Image srcImage = ImageIO.read(new File(srcImagePath));
      BufferedImage buffImg = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
          BufferedImage.TYPE_INT_BGR);
      Graphics2D g = buffImg.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g.drawImage(srcImage
              .getScaledInstance(srcImage.getWidth(null), srcImage.getHeight(null), Image.SCALE_SMOOTH),
          0, 0, null);
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
      g.drawImage(img, x, y, w, h, null);
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
      g.dispose();
      os = new FileOutputStream(targetPath);
      //生成图片
      ImageIO.write(buffImg, "JPG", os);
      logger.info("图片水印添加成功。。。");
    } catch (Exception e) {
      logger.error("图片水印添加失败。。。", e);
      throw e;
    } finally {
      try {
        if (null != os) {
          os.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

    public static void markBySignIcon(Image img, String srcImagePath, String targetPath, int x, int y,
                                      int w, int h) throws IOException {
        OutputStream os = null;
        try {
            logger.info("图片水印开始添加。。。");
            logger.info("图片水印输入路径" + srcImagePath);
            logger.info("图片水印输出路径" + targetPath);
            Image srcImage = ImageIO.read(new File(srcImagePath));
            BufferedImage buffImg = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
                    BufferedImage.TYPE_INT_BGR);
            Graphics2D g = buffImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImage
                            .getScaledInstance(srcImage.getWidth(null), srcImage.getHeight(null), Image.SCALE_SMOOTH),
                    0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
            g.drawImage(img, x, y, img.getWidth(null), img.getHeight(null), null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            os = new FileOutputStream(targetPath);
            //生成图片
            ImageIO.write(buffImg, "JPG", os);
            logger.info("图片水印添加成功。。。");
        } catch (Exception e) {
            logger.error("图片水印添加失败。。。", e);
            throw e;
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Image sign(BufferedImage image) {
    // TODO Auto-generated constructor stub
    try {
      logger.info("开始获取签名");
      ImageIcon imageIcon = new ImageIcon(image);
      BufferedImage bufferedImage = new BufferedImage(
          imageIcon.getIconWidth(), imageIcon.getIconHeight(),
          BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
      g2D.drawImage(imageIcon.getImage(), 0, 0,
          imageIcon.getImageObserver());
      int alpha;
      for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage
          .getHeight(); j1++) {
        for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage
            .getWidth(); j2++) {
          int rgb = bufferedImage.getRGB(j2, j1);
          if (colorInRange(rgb)) {
            alpha = 0;
          } else {
            alpha = 255;
          }
          rgb = (alpha << 24) | (rgb & 0x00ffffff);
          bufferedImage.setRGB(j2, j1, rgb);
        }
      }
      g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
      // 生成图片为PNG
      ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
      //加载图片
      ImageIO.write(bufferedImage, "png", byteArrayOut);
      imageIcon = new ImageIcon(byteArrayOut.toByteArray());
      Image img = imageIcon.getImage();
      logger.info("获取签名成功");
      return img;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static boolean colorInRange(int color) {
    int red = (color & 0xff0000) >> 16;
    int green = (color & 0x00ff00) >> 8;
    int blue = (color & 0x0000ff);
    return red >= 255 && green >= 255 && blue >= 255;
  }

  public static Boolean print(String printerName, String fileName) {
    logger.info("打印机名：" + printerName + " 文件名：" + fileName);
    FileInputStream fin = null;
    try {
      fin = new FileInputStream(fileName);
      DocFlavor dof = null;
      if (fileName.endsWith(".gif")) {
        dof = DocFlavor.INPUT_STREAM.GIF;
      } else if (fileName.endsWith(".jpg")) {
        dof = DocFlavor.INPUT_STREAM.JPEG;
      } else if (fileName.endsWith(".png")) {
        dof = DocFlavor.INPUT_STREAM.PNG;
      } else {
        dof = DocFlavor.INPUT_STREAM.JPEG;
      }
      AttributeSet hs = new HashAttributeSet();
      hs.add(new PrinterName(printerName, null));

      PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
      if (pss.length == 0) {
        logger.info("无法找到打印机");
        return false;
      }
      PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
      pras.add(OrientationRequested.PORTRAIT);

      pras.add(new Copies(1));
      pras.add(PrintQuality.HIGH);
      DocAttributeSet das = new HashDocAttributeSet();

      // 设置打印纸张的大小（以毫米为单位）
      das.add(new MediaPrintableArea(0, 0, 595, 842, MediaPrintableArea.MM));

      Doc doc = new SimpleDoc(fin, dof, das);

      DocPrintJob job = pss[0].createPrintJob();

      job.print(doc, pras);
      fin.close();
    } catch (Exception pe) {
      pe.printStackTrace();
      logger.info("打印异常");
      return false;
    } finally {
      if (fin != null) {
        try {
          fin.close();
        } catch (IOException e) {
          logger.info("打印异常");
          e.printStackTrace();
        }
      }
    }
    logger.info("打印成功");
    return true;
  }

    /**
     * 图片文件读取 * * @param srcImgPath * @return
     */
    private static BufferedImage inputImage(String srcImgPath) {
        BufferedImage srcImage = null;
        try {
            srcImage = javax.imageio.ImageIO.read(new URL(srcImgPath));
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
            e.printStackTrace();
        }
        return srcImage;
    }

    /**
     * * 将图片按照指定的图片尺寸压缩 * * @param srcImgPath :源图片路径 * @param outImgPath * :输出的压缩图片的路径 * @param new_w
     * * :压缩后的图片宽 * @param new_h * :压缩后的图片高
     */
    public static BufferedImage compressImage(String srcImgPath, int new_w, int new_h) {
        BufferedImage src = inputImage(srcImgPath);
        BufferedImage bufferedImage = disposeImage(src, new_w, new_h);
        return bufferedImage;
    }

    /**
     * 处理图片 * * @param src * @param outImgPath * @param new_w * @param new_h
     */
    private synchronized static BufferedImage disposeImage(BufferedImage src, int new_w, int new_h) {
        // 得到图片
        int old_w = src.getWidth();
        // 得到源图宽
        int old_h = src.getHeight();
        // 得到源图长
        BufferedImage newImg = null;
        // 判断输入图片的类型
        switch (src.getType()) {
            case 13:
                // png,gifnewImg = new BufferedImage(new_w, new_h,
                // BufferedImage.TYPE_4BYTE_ABGR);
                break;
            default:
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
                break;
        }
        Graphics2D g = newImg.createGraphics();
        // 从原图上取颜色绘制新图
        g.drawImage(src, 0, 0, old_w, old_h, null);
        g.dispose();
        // 根据图片尺寸压缩比得到新图的尺寸
        newImg.getGraphics().drawImage(
                src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0,
                null);
        // 调用方法输出图片文件
        return newImg;
    }

    /**
     * * 将图片文件输出到指定的路径，并可设定压缩质量 * * @param outImgPath * @param newImg * @param per
     */
    private static void OutImage(String outImgPath, BufferedImage newImg) {
        // 判断输出的文件夹路径是否存在，不存在则创建
        File file = new File(outImgPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }// 输出到文件流
        try {
            ImageIO.write(newImg, outImgPath.substring(outImgPath
                    .lastIndexOf(".") + 1), new File(outImgPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
  }
}
