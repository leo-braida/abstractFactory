package com.abstractFactory;

public class MemoryLogReader implements LogReader {
    StringBuilder log = new StringBuilder();

    @Override
    public void setLog(String log) {
        this.log = new StringBuilder(log);
    }

    @Override
    public StringBuilder getLog() {
        return this.log;
    }

    @Override
    public String readLog() {
        return this.log.toString();
    }
}
