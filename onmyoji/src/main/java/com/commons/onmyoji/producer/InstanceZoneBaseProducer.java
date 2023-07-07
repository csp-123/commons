package com.commons.onmyoji.producer;

import com.commons.onmyoji.config.OnmyojiScriptConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Title:
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2023/2/21 23:01
 */
@Component
public abstract class InstanceZoneBaseProducer<CONFIG extends OnmyojiScriptConfig> extends Thread implements InstanceZoneProducer<CONFIG>, KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.interrupt();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }
}
