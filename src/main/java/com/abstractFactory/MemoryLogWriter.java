package com.abstractFactory;

public class MemoryLogWriter implements LogWriter {
    @Override
    public String writeLog(StringBuilder log, String msg) {
        if (!log.isEmpty()) log.append("\n");
        log.append(msg);
        return "Successfully wrote log.";
    }
}
