package com.commons.designmodes.observer;

import com.alibaba.fastjson.JSON;

/**
 * @description: 当前气象状况面板
 * @author: cccsp
 * @date: 2022/10/18 22:47
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {
    /**
     * 信息
     */
    private String msg;

    /**
     * 主题
     */
    private Subject subject;

    public CurrentConditionsDisplay(Subject subject) {
        this.subject = subject;
        subject.registerObserver(this);
    }

    @Override
    public void update(Subject subject, Object arg) {
        if (subject instanceof WeatherData) {
            msg = ((WeatherData) subject).getMsg();
            display();
        }
    }

    @Override
    public void display() {
        System.out.println("当前数据：" + msg);
    }
}
