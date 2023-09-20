//package com.commons.onmyoji.utils;
//
//import org.opencv.core.Mat;
//import org.opencv.core.Size;
//import org.opencv.imgproc.Imgproc;
//
//import static org.opencv.imgcodecs.Imgcodecs.imread;
//
//public class OpenCV {
//
//    /** 均值哈希算法
//     * @param src 图片路径
//     * @return
//     */
//    public static char[] aHash(String src){
//        StringBuffer res = new StringBuffer();
//        try {
//            int width = 8;
//            int height = 8;
//            Mat mat = imread(src);
//            Mat resizeMat = new Mat();
//            Imgproc.resize(mat,resizeMat, new Size(width, height),0,0);
//            // 将缩小后的图片转换为64级灰度（简化色彩）
//            int total = 0;
//            int[] ints = new int[64];
//            int index = 0;
//            for (int i = 0;i < height;i++){
//                for (int j = 0;j < width;j++){
//                    int gray = gray(resizeMat.get(i, j));
//                    ints[index++] = gray;
//                    total = total + gray;
//                }
//            }
//            // 计算灰度平均值
//            int grayAvg = total / (width * height);
//            // 比较像素的灰度
//            for (int anInt : ints) {
//                if (anInt >= grayAvg) {
//                    res.append("1");
//                } else {
//                    res.append("0");
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return res.toString().toCharArray();
//    }
//
//    /** 感知哈希算法
//     * @param src
//     * @return
//     */
//    public static char[] pHash(String src){
//        int width = 8;
//        int height = 8;
//        Mat mat = imread(src);
//        Mat resizeMat = new Mat();
//        Imgproc.resize(mat,resizeMat, new Size(width, height),0,0);
//        int[] dctDate = new int[width * height];
//        int index = 0;
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                dctDate[index++] = gray(resizeMat.get(i, j));
//            }
//        }
//        dctDate = DCT.DCT(dctDate,width);
//        int avg = DCT.averageGray(dctDate ,width,height);
//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i<height; i++) {
//            for(int j=0; j<width; j++) {
//                if(dctDate[i*height + j] >= avg) {
//                    sb.append("1");
//                } else {
//                    sb.append("0");
//                }
//            }
//        }
//        long result;
//        if(sb.charAt(0) == '0') {
//            result = Long.parseLong(sb.toString(), 2);
//        } else {
//            //如果第一个字符是1，则表示负数，不能直接转换成long，
//            result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
//        }
//
//        sb = new StringBuilder(Long.toHexString(result));
//        if(sb.length() < 16) {
//            int n = 16-sb.length();
//            for(int i=0; i<n; i++) {
//                sb.insert(0, "0");
//            }
//        }
//        return sb.toString().toCharArray();
//    }
//
//    /** 差值哈希算法
//     * @param src
//     * @return
//     */
//    public static char[] dHash(String src){
//        int width = 9;
//        int height = 8;
//        Mat mat = imread(src);
//        Mat resizeMat = new Mat();
//        Imgproc.resize(mat,resizeMat, new Size(width, height),0,0);
//        int[] ints = new int[width * height];
//        int index = 0;
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                ints[index++] = gray(resizeMat.get(i, j));
//            }
//        }
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0;i < height;i++){
//            for (int j = 0;j < width - 1;j++){
//                if (ints[9 * j + i] >= ints[9 * j + i + 1]){
//                    builder.append(1);
//                }else {
//                    builder.append(0);
//                }
//            }
//        }
//        return builder.toString().toCharArray();
//    }
//
//    /** 简化色彩
//     * @param bgr
//     * @return
//     */
//    private static int gray(double[] bgr) {
//        int rgb = (int) (bgr[2] * 77 + bgr[1] * 151 + bgr[0] * 28) >> 8;
//        int gray = (rgb << 16) | (rgb << 8) | rgb;
//        return gray;
//    }
//
//    /** 计算汉明距离
//     * @param c1
//     * @param c2
//     * @return
//     */
//    private static int diff(char[] c1,char[] c2){
//        int diffCount = 0;
//        for (int i = 0; i < c1.length; i++) {
//            if (c1[i] != c2[i]) {
//                diffCount++;
//            }
//        }
//        return diffCount;
//    }
//}