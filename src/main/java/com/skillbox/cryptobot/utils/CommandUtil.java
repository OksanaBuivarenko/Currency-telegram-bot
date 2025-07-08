package com.skillbox.cryptobot.utils;

import com.skillbox.cryptobot.exception.CommandArgumentException;
import org.springframework.stereotype.Component;

@Component
public class CommandUtil {

    public static Double getCommandArgs(String text) {
        String[] arr = text.split("[,\\s]+");
        String numberStr = arr[1];

        String numberPattern = "^[+-]?\\d+(\\.\\d+)?$";

        if (!numberStr.matches(numberPattern)) {
            throw new CommandArgumentException("Введите число после /subscribe.");
        }
        return Double.parseDouble(numberStr);
    }
}
