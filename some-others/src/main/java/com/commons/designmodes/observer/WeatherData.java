package com.commons.designmodes.observer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 气象类
 * @author: cccsp
 * @date: 2022/10/18 22:39
 */
@Getter
public class WeatherData implements Subject{

    /**
     * 观察者
     *  练习demo使用内存存储，实际使用当中或许存库？
     */
    private List<Observer> observers;

    /**
     * 是否发生改变
     */
    private boolean changed;

    /**
     * 更新信息
     */
    private String msg;
    
    public WeatherData() {
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    @Override
    public void notifyObservers(Object arg) {
        if (changed) {
            observers.stream().forEach(observer -> observer.update(this, arg));
            changed = false;
        }
    }

    /**
     * @description:
     *      测量信息发生变化
     * @params: 无
     * @return: 无
     * @author: cccsp
     * @date: 2022/10/18 22:45
     */
    public void measurementsChanged() {
        setChanged();
        notifyObservers();
    }

    private void setChanged() {
        this.changed = true;
    }

    /**
     * @description:
     *      设置测量信息
     * @params: String msg 更新信息
     * @return: 无
     * @author: cccsp
     * @date: 2022/10/18 22:46
     */
    public void setMeasurements(String msg) {
        this.msg = msg;
        measurementsChanged();
    }
}
