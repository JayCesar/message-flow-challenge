package com.ibmmq.messageflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerModel {
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Level level;
    private final String MESSAGE;
    private LocalDateTime date;

    public LoggerModel(Level level, String MESSAGE, LocalDateTime date) {
        this.MESSAGE = MESSAGE;
        this.level = level;
        this.date = date;
        logEvent(level);
    }

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
        return String.format("LoggerModel{date='%s, level='%s', message='%s'}\n'",
                date.format(formatter), level, MESSAGE);
    }

}
