package com.commons.onmyoji.entity;

import com.commons.onmyoji.enums.HangUpTypeEnum;
import com.commons.onmyoji.enums.TeamTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


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
    private String windowNameList;

    /**
     * 开始执行时间
     */
    private LocalDateTime executeTime;

    public String getImgDirectory() {
        return System.getProperty("user.dir") + "\\onmyoji\\src\\main\\resources\\img\\" + this.getJobId() + "\\" + this.getTerminal() + "\\";
    }

}
