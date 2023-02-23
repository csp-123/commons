package com.commons.onmyoji.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;

/**
 * Title: 图片识别、处理工具类
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/22 13:16
 */
public class ImgKit {

    public static int CVTYPE = CvType.CV_8UC4;

    /**
     * 获取全屏幕
     * @return
     */
    public static BufferedImage getFullScreen() {
        BufferedImage bfImage = null;
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }

    /**
     * 获取屏幕截图
     * @return
     */
    public static Mat getScreenShot() {

        int captureWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int captureHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        BufferedImage bfImage =   new BufferedImage(captureWidth, captureHeight,      BufferedImage.TYPE_3BYTE_BGR);
        try {
            Robot robot = new Robot();

            Rectangle screenRect = new Rectangle(0,0,captureWidth,captureHeight);
            bfImage = robot.createScreenCapture(screenRect);

        } catch (AWTException e) {
            e.printStackTrace();
        }

        Mat mat =  new Mat(bfImage.getHeight(), bfImage.getWidth(), CvType.CV_8UC3);

        mat.put(0, 0,getMatrixRGB(bfImage));

        return mat;
    }

    /**
     * 获取图像RGB格式数据
     * @param image
     * @return
     */
    public static byte[] getMatrixRGB(BufferedImage image){
        if(image.getType()!=BufferedImage.TYPE_3BYTE_BGR){
            // 转sRGB格式
            BufferedImage rgbImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR);
            new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(image, rgbImage);
            // 从Raster对象中获取字节数组

            return  (byte[])((DataBufferByte)rgbImage.getRaster().getDataBuffer()).getData();
            //return (byte[]) rgbImage.getData().getDataElements(0, 0, rgbImage.getWidth(), rgbImage.getHeight(), null);
        }else{
            return (byte[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
        }
    }


    /**
     *  与目标进行匹配
     * @param source
     * @param targetPath
     */
    public static void findImg(Mat source, String targetPath) {
        // 被匹配的文件，读入为opencv 的mat格式
        Mat targetMat = Imgcodecs.imread(targetPath);

        // 创建与原图相同的大小，储存匹配度
        Mat result = Mat.zeros(source.rows() - targetMat.rows() + 1, source.cols() - targetMat.cols() + 1, CVTYPE);

        //调用模板匹配方法
        int method= Imgproc.TM_CCOEFF_NORMED;
        Imgproc.matchTemplate(source, targetMat, result, method);
        //规格化
        Core.normalize(targetMat, targetMat, 0, 1, Core.NORM_MINMAX, -1);
        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);

        org.opencv.core.Point matchLoc;

        //根据匹配算法选择匹配结果
        if (method==Imgproc.TM_SQDIFF||method==Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mlr.minLoc;
            System.out.println(" mlr.min="+mlr.minVal);//匹配度

        }
        else {
            matchLoc = mlr.maxLoc;
            System.out.println("mlr.max="+mlr.maxVal);//匹配度
        }

    }



    /**
     * 获取指定图片
     * @param path
     * @return
     */
    //public static BufferedImage getBfImgFromPath(String path) {
    //    BufferedImage bfImage = null;
    //    try {
    //        bfImage = ImageIO.read(new File(path));
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    return bfImage;
    //
    //}

    /**
     * 判断屏幕截图上目标图范围内的全部点是否全部和小图的点一一对应
     * @param y
     * @param x
     * @return
     */
    //public static boolean isMatchAll(int y, int x) {
    //
    //    int bigpY = 0;
    //    int bigpX = 0;
    //    int judeg= 0;
    //    for(int smallpY=0; smallpY<keyImgHeight; smallpY++) {
    //        bigpY = y+smallpY;
    //        for(int smallpX=0; smallpX<keyImgWidth; smallpX++) {
    //            bigpX = x+smallpX;
    //            if(bigpY>=1080 || bigpX>=1920) {
    //                return false;
    //            }
    //            judeg = keyImageRGBData[smallpY][smallpX]^screenShotImageRGBData[bigpY][bigpX];
    //            if(judeg!=0) {
    //                return false;
    //            }
    //        }
    //    }
    //    return true;
    //}

}
