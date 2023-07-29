//package com.commons.someothers.someutils;
//
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Title:   视频转换工具
// * Description:
// *  核心是利用ffmpeg进行视频转换，我们自己并不写转换视频的代码，只是调用ffmpeg，它会帮我们完成视频的转换。
// *  ffmpeg支持的类型有：asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等，这些类型，可以利用ffmpeg进行直接转换。
// *  ffmpeg不支持的类型有：wmv9，rm，rmvb等，这些类型需要先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式。
// * Project: commons
// * Author: csp
// * Create Time:2022/10/14 14:01
// */
//public class VideoConvertUtil {
//
//    //ffmpeg存放的路径
//    public static final String ffmpegPath = "D:\\APPSource\\ffmpeg\\bin\\ffmpeg.exe";
//    //mencoder存放的路径
//    public static final String mencoderPath = "D:\\APPSource\\MPlayer\\mencoder.exe";
//    //通过mencoder转换成的avi存放路径
//    public static final String aviFilepath = "D:\\videos\\avi\\temp.avi";
//
//    // ffmpeg 支持转换的文件
//    public static final List<String> canBeConvertByFfmpeg = new ArrayList<>(Arrays.asList("asx", "asf", "mpg", "wmv", "3gp", "mp4", "mov", "avi", "flv"));
//
//    // ffmpeg 不支持转换的文件
//    public static final List<String> canNotBeConvertByFfmpeg = new ArrayList<>(Arrays.asList("wmv9", "rm", "rmvb"));
//
//
//
//    public static void main(String[] args) {
//
//        String filePath = "D:\\videos\\tobeconvert";
//
//        String input = "06e02bc16002749c40e6ec8b0156e66c.mp4";
//        String output = "test.wmv";
//
//
//        boolean success = convert(filePath + "\\" + input, filePath + "\\" + output);
//
//        System.out.println(success ? "success is over" : "oh no failed");
//    }
//
//    /**
//     * @param inputFile:需要转换的视频
//     * @param outputFile：转换后的视频w
//     * @return
//     */
//    public static boolean convert(String inputFile, String outputFile) {
//        if (isFile(inputFile)) {
//            return process(inputFile, outputFile);
//        }
//        System.out.println(inputFile + " is not file");
//        return false;
//
//    }
//
//    // 检查文件是否存在
//    private static boolean isFile(String path) {
//        File file = new File(path);
//        return file.isFile();
//    }
//
//    /**
//     * @param inputFile:需要转换的视频
//     * @param outputFile：转换后的视频w
//     * @return
//     * 转换视频文件
//     */
//    private static boolean process(String inputFile, String outputFile) {
//
//        boolean canBeConvert = false;
//        try {
//            canBeConvert = canBeConvert(inputFile);
//        } catch (CommonsException e) {
//            System.out.println(e.getMessage());
//        }
//
//        if (canBeConvert) {
//            return processFLV(inputFile, outputFile);// 直接将文件转为flv文件
//        } else {
//            String aviFilepath = processAVI(inputFile);
//            if (aviFilepath == null)
//                return false;// avi文件没有得到
//            return processFLV(aviFilepath, outputFile);// 将avi转为flv
//        }
//
//    }
//
//    private static boolean canBeConvert(String inputFile) throws CommonsException {
//
//        String type = inputFile.substring(inputFile.lastIndexOf(".") + 1).toLowerCase();
//
//        if (canBeConvertByFfmpeg.contains(type)) {
//            return true;
//        }
//
//        if (canNotBeConvertByFfmpeg.contains(type)) {
//            return false;
//        }
//
//        throw new CommonsException(CommonsErrorCode.UNKNOWN_ERROR,"不支持的格式");
//    }
//
//
//    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）直接转换为目标视频
//    private static boolean processFLV(String inputFile, String outputFile) {
//        if (!isFile(inputFile)) {
//            System.out.println(inputFile + " is not file");
//            return false;
//        }
//        List<String> commend = new ArrayList<>();
//
//        commend.add(ffmpegPath);
//        commend.add("-i");
//        commend.add(inputFile);
//        commend.add("-ab");
//        commend.add("128");
//        commend.add("-acodec");
//        commend.add("libmp3lame");
//        commend.add("-ac");
//        commend.add("1");
//        commend.add("-ar");
//        commend.add("22050");
//        commend.add("-r");
//        commend.add("29.97");
//        //高品质
//        commend.add("-qscale");
//        commend.add("6");
//        //低品质
//        //   commend.add("-b");
//        //   commend.add("512");
//        commend.add("-y");
//
//        commend.add(outputFile);
//        StringBuffer test = new StringBuffer();
//        for (int i = 0; i < commend.size(); i++) {
//            test.append(commend.get(i) + " ");
//        }
//
//        System.out.println(test);
//
//        try {
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.command(commend);
//            builder.start();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
//    // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
//    private static String processAVI(String inputFile) {
//        File file = new File(aviFilepath);
//        if (file.exists())
//            file.delete();
//        List<String> commend = new ArrayList<String>();
//        commend.add(mencoderPath);
//        commend.add(inputFile);
//        commend.add("-oac");
//        commend.add("mp3lame");
//        commend.add("-lameopts");
//        commend.add("preset=64");
//        commend.add("-ovc");
//        commend.add("xvid");
//        commend.add("-xvidencopts");
//        commend.add("bitrate=600");
//        commend.add("-of");
//        commend.add("avi");
//        commend.add("-o");
//        commend.add(aviFilepath);
//        StringBuffer test = new StringBuffer();
//        for (int i = 0; i < commend.size(); i++) {
//            test.append(commend.get(i) + " ");
//        }
//
//        System.out.println(test);
//        try {
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.command(commend);
//            Process p = builder.start();
//
//            final InputStream is1 = p.getInputStream();
//            final InputStream is2 = p.getErrorStream();
//            new Thread(() -> {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(is1));
//                try {
//                    String lineB = null;
//                    while ((lineB = br.readLine()) != null) {
//                        if (lineB != null)
//                            System.out.println(lineB);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//            new Thread(() -> {
//                BufferedReader br2 = new BufferedReader(
//                        new InputStreamReader(is2));
//                try {
//                    String lineC = null;
//                    while ((lineC = br2.readLine()) != null) {
//                        if (lineC != null)
//                            System.out.println(lineC);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//
//            // 等Mencoder进程转换结束，再调用ffmepg进程
//            p.waitFor();
//            System.out.println("who cares");
//            return aviFilepath;
//        } catch (Exception e) {
//            System.err.println(e);
//            return null;
//        }
//    }
//
//
//
//}
