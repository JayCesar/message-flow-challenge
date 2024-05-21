package com.ibmmq.messageflow.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggerModelTest {

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        LoggerModel logger = new LoggerModel(Level.INFO, "Test message", now);

        String expectedLogMessage = String.format("LoggerModel{date='%s', level='%s', message='Test message'}'\n",
                now.format(logger.formatter), Level.INFO);

        assertEquals(expectedLogMessage, logger.toString());
    }
}
