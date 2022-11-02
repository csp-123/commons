package com.commons.designmodes.observer.main;

import com.commons.designmodes.observer.CurrentConditionsDisplay;
import com.commons.designmodes.observer.WeatherData;

/**
 * @description: 气象台
 * @author: cccsp
 * @date: 2022/10/18 22:51
 */
public class WeatherStation {



    public static void main(String[] args) throws InterruptedException {

        WeatherData weatherData = new WeatherData();

        CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData);

        weatherData.setMeasurements("初始信息1");

        Thread.sleep(5000);
        weatherData.setMeasurements("更新信息1");

        Thread.sleep(3000);

        weatherData.setMeasurements("更新信息2");


    }
}
