package com.commons.onmyoji.entity;

import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import com.commons.onmyoji.utils.ImageSimilarityUtil;
import com.google.protobuf.ServiceException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/20 16:32
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class OnmyojiJob {

    /**
     * jobId
     */
    private String jobId;

    /**
     * 名称
     */
    private String jobName;

    /**
     * 组队类型
     */
    private TeamTypeEnum teamType;

    /**
     * 挂机类型
     */
    private HangUpTypeEnum hangUpType;

    /**
     * 次数
     */
    private Integer times;

    /**
     * 时长
     */
    private Integer time;

    /**
     * 处理器
     */
    private String producerName;

    /**
     * 设备
     */
    private String terminal;

    /**
     * 窗口名称
     */
    private List<String> windowNameList;

    /**
     * 开始执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 图片数据
     */
    List<Picture> pictureList;

    public String getImgDirectory() {
        return System.getProperty("user.dir") + "\\onmyoji\\src\\main\\resources\\img\\" + this.getTerminal() + "\\" + this.getJobId() + "\\";
    }

    /**
     * 加载图片信息
     * @throws ServiceException
     */
    public void loadPictures() throws ServiceException {
        File parentPath = new File(getImgDirectory());
        File[] files = parentPath.listFiles();
        if (files == null || files.length == 0) {
            throw new ServiceException("目录下无文件");
        }
        List<String> imgPaths = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
        List<Picture> pictures = imgPaths.stream().map(targetImgPath -> {
            BufferedImage bfImage = ImageSimilarityUtil.getBfImageFromPath(targetImgPath);
            int[][] imageRGB = ImageSimilarityUtil.getImageRGB(bfImage);
            Picture picture = new Picture();
            picture.setBf(bfImage);
            picture.setImgParentPath(getImgDirectory());
            picture.setImgPath(targetImgPath);
            picture.setRgb(imageRGB);
            String pictureName = ImageSimilarityUtil.getNameFromPath(targetImgPath);
            picture.setLocate(pictureName.endsWith("_start"));
            picture.setImgName(pictureName);
            return picture;
        }).collect(Collectors.toList());
        this.setPictureList(pictures);
    }

    @Override
    public String toString() {
        return String.format("任务id：%s，任务名称：%s，组队类型：%s，挂机类型：%s，次数：%s，时长：%s，处理器：%s，设备：%s",
                this.getJobId(), this.getJobName(), this.getTeamType(), this.getHangUpType(), this.getTimes(), this.getTime(),
                this.getProducerName(), this.getTerminal());
    }
}
