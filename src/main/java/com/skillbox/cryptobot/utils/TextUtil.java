package com.skillbox.cryptobot.utils;

import org.springframework.stereotype.Component;

@Component
public class TextUtil {

    public static String toString(double value) {
        return String.format("%.3f", value);
    }

}
