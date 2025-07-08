package com.skillbox.cryptobot.exception;

public class CommandArgumentException extends RuntimeException {
    public CommandArgumentException(String message) {
        super(message);
    }
}
