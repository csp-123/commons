package com.commons.onmyoji.utils;

import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @description: 测试
 * @author: cccsp
 * @date: 2023/2/23 17:08
 */
public class KeyListenerTest extends JFrame implements KeyListener {
    public KeyListenerTest() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyChar() ==KeyEvent.VK_ENTER) {
                    System.out.println("ENTER");
                }
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("typed");
        System.out.println(e.getKeyCode());
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            System.out.println("监测到退出指令，程序即将推出");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");

        System.out.println(e.getKeyCode());
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            System.out.println("监测到退出指令，程序即将推出");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased");

        System.out.println(e.getKeyCode());
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            System.out.println("监测到退出指令，程序即将推出");
        }
    }
}

