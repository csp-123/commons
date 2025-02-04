package com.commons.onmyoji.components;

import com.commons.onmyoji.job.RunningJobPool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @description: 测试
 * @author: cccsp
 * @date: 2023/2/23 17:08
 */

public class OnmyojiKeyListenerV2 implements KeyListener,Runnable {


    private boolean isRunning;

    public OnmyojiKeyListenerV2() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("typed");
        System.out.println(e.getKeyCode());
//        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
//            System.out.println("监测到退出指令，程序即将推出");
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");
        System.out.println(e.getKeyCode());
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            stop();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased");

        System.out.println(e.getKeyCode());
//        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
//            System.out.println("监测到退出指令，程序即将推出");
//        }
    }

    @Override
    public void run() {
        while (isRunning) {
            System.out.println("-----");
        }
    }
}

