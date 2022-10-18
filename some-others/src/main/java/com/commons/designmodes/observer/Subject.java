package com.commons.designmodes.observer;

/**
 * @description: 主题
 * @author: cccsp
 * @date: 2022/10/16 22:29
 */
public interface Subject {

    /**
     * @description: 注册观察者
     * @params: Observer observer 观察者
     * @return: 无
     * @author: cccsp
     * @date: 2022/10/18 22:32
     */
    void registerObserver(Observer observer);

    /**
     * @description:
     *      移除观察者
     * @params: Observer observer 观察者
     * @return: 无
     * @author: cccsp
     * @date: 2022/10/18 22:33
     */
    void removeObserver(Observer observer);

    /**
     * @description:
     *      通知观察者
     * @params: null
     * @return: 无
     * @author: cccsp
     * @date: 2022/10/18 22:34
     */
    void notifyObservers();


    void notifyObservers(Object arg);
}
