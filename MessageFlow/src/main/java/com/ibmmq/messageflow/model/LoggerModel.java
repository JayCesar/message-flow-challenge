package com.ibmmq.messageflow.model;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerModel {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
    private Level level;
    private final String MESSAGE;

    public LoggerModel(Level level, String MESSAGE) {
        this.MESSAGE = MESSAGE;
        this.level = level;
        logEvent(level);
    }

    public LoggerModel(Level level, String MESSAGE, Exception exception) {
        this.MESSAGE =
                "Name " + exception.getClass().getName() + " Message: " + exception.getMessage();
    }

    public void logEvent(Level level){
        logger.log(level, this.toString());
    }

    @Override
    public String toString() {
        return "LoggerModel{" +
                "MESSAGE='" + MESSAGE + '\'' +
                ", level=" + level +
                '}';
    }




}
