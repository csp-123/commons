package com.commons.onmyoji.utils;
 
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.context.Theme;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Thread.sleep;
 
/**
 * Created by long.tang on 2019/4/25.
 */
@Getter
public class FindRobot {

    private static final Logger logger = LoggerFactory.getLogger(FindRobot.class);
 
    BufferedImage screenShotImage = this.getFullScreenShot();    //屏幕截图
    BufferedImage keyImage;           //查找目标图片
    int scrShotImgWidth;              //屏幕截图宽度
    int scrShotImgHeight;             //屏幕截图高度
    int keyImgWidth;                  //查找目标图片宽度
    int keyImgHeight;                 //查找目标图片高度
    int[][] screenShotImageRGBData;   //屏幕截图RGB数据
    int[][] keyImageRGBData;          //查找目标图片RGB数据
    int findX;            //查找结果，目标图标位于屏幕截图上的X坐标数据
    int findY;            //查找结果，目标图标位于屏幕截图上的Y坐标数据
    private static  Robot robot = getRobot();

    /**
     * 所有匹配点
     */
    public static  Map<String, String> map = new LinkedHashMap<>();

    private boolean found = false;
    public FindRobot(){
 
    }
    /**
     * 是否改变目标背景画布再次查找
     * @param keyImagePath
     */
    public FindRobot(String keyImagePath,String screenshot,int x , int y) {
        screenShotImage = (screenshot==null || screenshot.equals(""))?this.getFullScreenShot():this.getBfImageFromPath(screenshot);
        keyImage = this.getBfImageFromPath(keyImagePath);

        screenShotImageRGBData = this.getImageGRB(screenShotImage);
        keyImageRGBData = this.getImageGRB(keyImage);
        scrShotImgWidth = screenShotImage.getWidth();
        scrShotImgHeight = screenShotImage.getHeight();
        keyImgWidth = keyImage.getWidth();
        keyImgHeight = keyImage.getHeight();
        //开始查找
        this.findImageXY(x,y);
    }
 
 
 
    /**
     * 查找图片
     */
    public void findImageXY(int a ,int b) {
        map.clear();
        //遍历屏幕截图像素点数据
        for (int y = b; y < scrShotImgHeight - keyImgHeight; y++) {
            for (int x = a; x < scrShotImgWidth - keyImgWidth; x++) {
                //根据目标图的尺寸，得到目标图四个角映射到屏幕截图上的四个点，
                //判断截图上对应的四个点与图B的四个角像素点的值是否相同，
                //如果相同就将屏幕截图上映射范围内的所有的点与目标图的所有的点进行比较。
                if ((keyImageRGBData[0][0] ^ screenShotImageRGBData[y][x]) == 0
                        && (keyImageRGBData[0][keyImgWidth - 1] ^ screenShotImageRGBData[y][x + keyImgWidth - 1]) == 0
                        && (keyImageRGBData[keyImgHeight - 1][keyImgWidth - 1] ^ screenShotImageRGBData[y + keyImgHeight - 1][x + keyImgWidth - 1]) == 0
                        && (keyImageRGBData[keyImgHeight - 1][0] ^ screenShotImageRGBData[y + keyImgHeight - 1][x]) == 0) {
 
                    boolean isFinded = isMatchAll(y, x);
                    //如果比较结果完全相同，则说明图片找到，填充查找到的位置坐标数据到查找结果数组。
                    this.found = isFinded;

                    if (isFinded) {
                        //0
                        int minY = y;
                        int maxY = y + keyImgHeight;
                        findY = ((minY + maxY) / 2);
                        //1
                        int minX = x;
                        int maxX = x + keyImgWidth;
                        findX = ((minX + maxX) / 2);
                        //System.out.println("找到第"+findX+"次");
                        map.put("point_"+findX+"_"+findY,findX+","+findY);
                        //return;
                    }
                }
            }
        }
    }
 
 
 
 
    /**
     * 获取机器人
     * @return
     */
    private static Robot getRobot(){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return robot;
    }
 
    /**
     * 全屏截图
     *
     * @return 返回BufferedImage
     */
    public BufferedImage getFullScreenShot() {
        BufferedImage bfImage = null;
        int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }
 
    /**
     * 输出图片文件
     * @param bi
     */
    public static String writeImageFile(BufferedImage bi) {
        String path = "";
        try {
            path = "/files/screen_"+ System.currentTimeMillis()+".png";
            File outputfile = new File(path);
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException e) {
 
        }
        return path;
    }
 
 
    /**
     * 从本地文件读取目标图片
     *
     * @param keyImagePath - 图片绝对路径
     * @return 本地图片的BufferedImage对象
     */
    public BufferedImage getBfImageFromPath(String keyImagePath) {
        BufferedImage bfImage = null;
        try {
            bfImage = ImageIO.read(new File(keyImagePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bfImage;
    }
 
    /**
     * 根据BufferedImage获取图片RGB数组
     *
     * @param bfImage
     * @return
     */
    public int[][] getImageGRB(BufferedImage bfImage) {
        int width = bfImage.getWidth();
        int height = bfImage.getHeight();
        int[][] result = new int[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                //使用getRGB(w, h)获取该点的颜色值是ARGB，而在实际应用中使用的是RGB，所以需要将ARGB转化成RGB，即bufImg.getRGB(w, h) & 0xFFFFFF。
                result[h][w] = bfImage.getRGB(w, h) & 0xFFFFFF;
            }
        }
        return result;
    }
 
    /**
     * 判断屏幕截图上目标图映射范围内的全部点是否全部和小图的点一一对应。
     *
     * @param y - 与目标图左上角像素点想匹配的屏幕截图y坐标
     * @param x - 与目标图左上角像素点想匹配的屏幕截图x坐标
     * @return
     */
    public boolean isMatchAll(int y, int x) {
        int biggerY = 0;
        int biggerX = 0;
        int xor = 0;
        for (int smallerY = 0; smallerY < keyImgHeight; smallerY++) {
            biggerY = y + smallerY;
            for (int smallerX = 0; smallerX < keyImgWidth; smallerX++) {
                biggerX = x + smallerX;
                if (biggerY >= scrShotImgHeight || biggerX >= scrShotImgWidth) {
                    return false;
                }
                xor = keyImageRGBData[smallerY][smallerX] ^ screenShotImageRGBData[biggerY][biggerX];
                if (xor != 0) {
                    //hitMiss++;
                    return false;
                }
            }
            //biggerX = x;
        }
 
        return true;
    }
 
 
 
 
 
    /*****
     * //移动鼠标
     * robot.mouseMove(500, 500);
     * //点击鼠标
     * //鼠标左键
     * System.out.println("单击");
     * robot.mousePress(InputEvent.BUTTON1_MASK);
     * robot.mouseRelease(InputEvent.BUTTON1_MASK);
     * //鼠标右键
     * System.out.println("右击");
     * robot.mousePress(InputEvent.BUTTON3_MASK);
     * robot.mouseRelease(InputEvent.BUTTON3_MASK);
     * //按下ESC，退出右键状态
     * System.out.println("按下ESC");
     * robot.keyPress(KeyEvent.VK_ESCAPE);
     * robot.keyRelease(KeyEvent.VK_ESCAPE);
     * //滚动鼠标滚轴
     * System.out.println("滚轴");
     * robot.mouseWheel(5);
     * <p>
     * <p>
     * //按下Alt+TAB键（切换桌面窗口）
     * robot.keyPress(KeyEvent.VK_ALT);
     * for(int i=1;i<=2;i++)
     * {
     * robot.keyPress(KeyEvent.VK_TAB);
     * robot.keyRelease(KeyEvent.VK_TAB);
     * }
     * robot.keyRelease(KeyEvent.VK_ALT);
     * robot.mouseWheel 滚动鼠标
     * @param path
     */
    public static boolean touchPic(String path) {
        //移动到定位
        boolean res = findPoint(path);

        if(!res){
            logger.info("移动鼠标失败");
            return false;
        } else {
            logger.info("移动鼠标成功");
        }
        // 移动后随机停顿100-130ms
        waitSomeTime(100, 130);
        leftClick(0);
        logger.info("点击鼠标完成");
        return true;
    }


    /*****
     * //移动鼠标
     * robot.mouseMove(500, 500);
     * //点击鼠标
     * //鼠标左键
     * System.out.println("单击");
     * robot.mousePress(InputEvent.BUTTON1_MASK);
     * robot.mouseRelease(InputEvent.BUTTON1_MASK);
     * //鼠标右键
     * System.out.println("右击");
     * robot.mousePress(InputEvent.BUTTON3_MASK);
     * robot.mouseRelease(InputEvent.BUTTON3_MASK);
     * //按下ESC，退出右键状态
     * System.out.println("按下ESC");
     * robot.keyPress(KeyEvent.VK_ESCAPE);
     * robot.keyRelease(KeyEvent.VK_ESCAPE);
     * //滚动鼠标滚轴
     * System.out.println("滚轴");
     * robot.mouseWheel(5);
     * <p>
     * <p>
     * //按下Alt+TAB键（切换桌面窗口）
     * robot.keyPress(KeyEvent.VK_ALT);
     * for(int i=1;i<=2;i++)
     * {
     * robot.keyPress(KeyEvent.VK_TAB);
     * robot.keyRelease(KeyEvent.VK_TAB);
     * }
     * robot.keyRelease(KeyEvent.VK_ALT);
     * robot.mouseWheel 滚动鼠标
     * @param path
     */
    public static boolean touchAllPic(String path) {
        map.clear();
        FindRobot findRobot = new FindRobot(path, null, 0, 0);
        logger.info("匹配结果" + JSON.toJSONString(map));
        for (String value : FindRobot.map.values()) {
            String[] location = value.split(",");
            int x = Integer.parseInt(location[0]);
            int y = Integer.parseInt(location[1]);
            int randomX = buildRandomLocation(x, findRobot.keyImgWidth);
            int randomY = buildRandomLocation(y, findRobot.keyImgHeight);
            robot.mouseMove(randomX, randomY);
            logger.info("移动鼠标成功");
            // 停顿随机时间 100 - 130 ms
            waitSomeTime(100, 130);
            leftClick(0);
            logger.info("点击鼠标成功");
            // 多开时停顿
            waitSomeTime(200, 400);
        }

        return true;
    }

    /**
     * 等待一段时间
     * @param time1
     * @param time2
     */
    private static void waitSomeTime(int time1, int time2) {
        Random random = new Random();
        int max = Math.max(time1, time2);
        int min = Math.max(time1, time2);
        int randomTime = random.nextInt(max - min + 1) + min;

        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 找到定位并移动到定位点
     * @param path
     * @return
     */
    public static boolean findPoint(String path){
        return findPoint(path,true);
    }
 
    public static boolean findPoint(String path,boolean move){
        map.clear();
        FindRobot demo = new FindRobot(path,"",0,0);
        if(map==null||map.size()==0){
            logger.info("未找到");
            return false;
        } else {
            logger.info("找到:"+demo.findX+","+demo.findY);
            // 移动图像内随机位置
            if(move){
                int imgHeight = demo.getKeyImgHeight();
                int imgWidth = demo.getKeyImgWidth();
                int clickX = buildRandomLocation(demo.findX, imgWidth);
                int clickY = buildRandomLocation(demo.findY, imgHeight);
                logger.info("移动到位置:"+clickX+","+clickY);
                robot.mouseMove(clickX, clickY);
            }
            return true;
        }
    }

    /**
     * 以中心点为构建随机位置
     *      例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     *      得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     * @param position 像素单维度坐标
     * @param length 图像单维度长度
     * @return 最终坐标
     */
    private static int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 查找是否唯一解
     * @param path
     * @return
     */
 
    public static boolean findAllSamePoint(String path,String screen,int x ,int y){
        FindRobot demo = new FindRobot(path,screen,x,y);
 
        if(demo.findX==0 && demo.findY==0){
            return false;
        } else {
            //System.out.println("找到:"+demo.findX+","+demo.findY);
            findAllSamePoint(path,screen,demo.findX,demo.findY);
        }
 
        return true;
    }
 
 
 
    /**
     * 输入键盘对应值
     * KeyEvent.VK_*
     * @param keyPress
     */
    public static boolean inputVaule(int keyPress){
        downKeyPress(keyPress);
        setAutoDelay(100);
        upKeyPress(keyPress);
        return true;
    }
 
    /**
     * 设置休息时间，毫秒
     * @param time
     * @return
     */
    public static void setAutoDelay(int time){
        robot.setAutoDelay(time);
    }
 
    /**
     * 按下键位
     * @param keyPress
     */
    public static void downKeyPress(int keyPress){
        setAutoDelay(100);
        robot.keyPress(keyPress);
    }
 
    /**
     * 释放按键
     * @param keyPress
     */
    public static void upKeyPress(int keyPress){
        setAutoDelay(100);
        robot.keyRelease(keyPress);
    }
 
 
    /**
     * 左击测试
     */
    public static void leftClick(int time){
        leftDown();
        setAutoDelay(time==0?100:time);
        leftUp();
    }
 
 
    /**
     * 左击按下
     */
    public static void leftDown() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }
    /**
     * 左击释放
     */
    public static void leftUp() {
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
 
    /**
     * 右击测试
     */
    public static void rightKey(int time){
        robot.mousePress(InputEvent.BUTTON3_MASK);
        setAutoDelay(time==0?100:time);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }
 
    /**
     * 移动鼠标
     * @param x
     * @param y
     */
    public static void move(int x ,int y){
        robot.mouseMove(x,y);
    }
 
 
    public static Map<String,Object> findComparePic(String touchPic,String screenPic){
        findAllSamePoint(touchPic,screenPic,0,0);
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("result",map.size()==0?0:1);
        result.put("message",map.size()>0?"匹配到"+map.size()+"个坐标":"没有匹配到坐标点");
        result.put("points",map);
        return result;
    }
 
    /**
     * 截屏,远程默认800*600
     ============BAT=============
     @%windir%\System32\tscon.exe 0 /dest:console
     @%windir%\System32\tscon.exe 1 /dest:console
     @%windir%\System32\tscon.exe 2 /dest:console
     ==========================
     for /f "skip=1 tokens=3 usebackq" %%s in (
     `query user %username%`
     ) do (
     %windir%\System32\tscon.exe %%s /dest:console
     )
     --------------------------
     * @return
     */
    public static String printScreen() {
        FindRobot findRobot = new FindRobot();
        BufferedImage bufferedImage=findRobot.getFullScreenShot();
        return writeImageFile(bufferedImage);
    }
 
    public static void scroll(Integer x) {
        robot.mouseWheel(x);
    }

 
 
}