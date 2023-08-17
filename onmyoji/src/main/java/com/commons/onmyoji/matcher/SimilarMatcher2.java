package com.commons.onmyoji.matcher;

import com.commons.onmyoji.utils.ImageSimilarity;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description:
 * 单刷图片匹配器
 * Author: chish
 * Date: 2023/3/12 1:07
 */
@Getter
public class SimilarMatcher2 {

    private static final Logger logger = LoggerFactory.getLogger(SimilarMatcher2.class);

    private ExecutorService executorService = new ThreadPoolExecutor(1, 3, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<>(20));

    private static final Robot robot = getRobot();

    // 近似匹配阈值
    private static final double SIMILAR_THRESHOLD = 0.9D;

    /**
     * BufferedImage数据缓存
     */
    private static Map<String, int[][]> RGBDataMap = new HashMap<>(16);


    /**
     * BufferedImage数据缓存
     */
    private static Map<String, BufferedImage> bfImageMap = new HashMap<>(16);

    /**
     * 来源图片路径
     */
    private List<String> targetImgPathList;

    /**
     * 匹配结果
     */
    private Map<String, List<MatchResult>> results;

    public SimilarMatcher2(List<String> targetImgPathList) {
        this.targetImgPathList = targetImgPathList;
        this.load(targetImgPathList);
    }

    /**
     * 初始化加载
     *
     * @param targetImgPathList
     */
    private void load(List<String> targetImgPathList) {
        // 加载RGB缓存
        for (String targetImgPath : targetImgPathList) {
            BufferedImage bfImage = getBfImageFromPath(targetImgPath);
            int[][] imageRGB = getImageRGB(bfImage);
            bfImageMap.put(targetImgPath, bfImage);
            RGBDataMap.put(targetImgPath, imageRGB);
        }
    }

    @SneakyThrows
    public void similarMatchAndClick(String targetImgPath, boolean solo) {
        int[][] RGBData = RGBDataMap.get(targetImgPath);
        BufferedImage bufferedImage = bfImageMap.get(targetImgPath);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        // 近似匹配规则：
        // 灰度匹配，相似度超过阈值即认定为匹配成功
        // 单刷模式下 找到一个点位即返回

        // todo  不要遍历整个屏幕，应获取游戏进程，拿到游戏窗口位置，根据位置和窗口大小遍历，而且要避免遍历像素点，别把cpu干废了。
        /**
         * 是否可以利用相似度二分法实现？
         */



    }

    public void test() {
        try {
            String line;
//            Process p = Runtime.getRuntime().exec("ps -e");
            Process p = Runtime.getRuntime().exec
                    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line); //<-- Parse data here.
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
     * 以中心点为构建随机位置
     * 例：（125，226） 64*58 横坐标：position = 125，横向长度： size = 64
     * 得出最终横坐标 125-(64/2)<result<125+(64/2) 即   93<result<157
     *
     * @param position 像素单维度坐标
     * @param length   图像单维度长度
     * @return 最终坐标
     */
    private static int buildRandomLocation(int position, int length) {
        Random random = new Random();
        int min = position - length / 2;
        int max = position + length / 2;
        return random.nextInt(max - min + 1) + min;
    }


    /**
     * 从本地文件读取目标图片
     *
     * @param keyImagePath - 图片绝对路径
     * @return 本地图片的BufferedImage对象
     */
    private BufferedImage getBfImageFromPath(String keyImagePath) {
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
    private static int[][] getImageRGB(BufferedImage bfImage) {
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

    private static class MatchResult {

        /**
         * x坐标
         */
        private Integer locationX;

        /**
         * Y坐标
         */
        private Integer locationY;

        MatchResult(Integer x, Integer y) {
            locationX = x;
            locationY = y;
        }

        public Integer getLocationX() {
            return locationX;
        }

        public void setLocationX(Integer locationX) {
            this.locationX = locationX;
        }

        public Integer getLocationY() {
            return locationY;
        }

        public void setLocationY(Integer locationY) {
            this.locationY = locationY;
        }
    }


    /**
     * 获取robot对象
     *
     * @return Robot
     */
    private static Robot getRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }


}