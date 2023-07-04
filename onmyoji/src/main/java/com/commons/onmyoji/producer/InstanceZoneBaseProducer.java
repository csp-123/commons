package com.commons.onmyoji.producer;

import com.commons.onmyoji.config.OnmyojiScriptConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:01
 */
@Component
public abstract class InstanceZoneBaseProducer<CONFIG extends OnmyojiScriptConfig> implements InstanceZoneProducer<CONFIG> {

    /**
     * 屏幕高度 默认1080
     */
    @Value("${screen.height}")
    public int height = 1600;

    /**
     * 屏幕高度 默认1920
     */
    @Value("${screen.width}")
    public final int width = 2560;


}
