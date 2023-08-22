package com.commons.onmyoji.config;

import lombok.Data;

import java.util.List;

/**
 * Title: 脚本配置
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 22:41
 */
@Data
public abstract class OnmyojiScriptConfig {

    /**
     * 脚本涉及图片位置
     */
    public String imgPath;

    /**
     * 模拟器窗口名称
     */
    private List<String> windowNameList;

}
