package com.commons.designmodes.observer;
/**
 * @description:
 *      观察者
 * @author: cccsp
 * @date: 2022/10/16 22:30
 */
public interface Observer {
    /**
     * @description:
     *      更新
     * @params: Subject subject 主题
     * @params: Object arg 更新信息
     * @return: wu
     * @author: cccsp
     * @date: 2022/10/18 22:39
     */
    void update(Subject subject, Object arg);
}
