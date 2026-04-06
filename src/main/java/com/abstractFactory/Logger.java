package com.abstractFactory;

import java.time.Instant;

public class Logger {
    private LogReader reader;
    private LogWriter writer;

    public Logger(LogAbstractFactory factory) {
        this.reader = factory.createReader();
        this.writer = factory.createWriter();
    }

    public void setLog(String log) {
        this.reader.setLog(log);
    }

    public StringBuilder getLog() {
        return this.reader.getLog();
    }

    public String writeLog(String origin, String msg) throws Exception {
        String fullMsg = Instant.now() + " - [" + origin + "]: " + msg;
        try {
            return this.writer.writeLog(this.reader.getLog(), fullMsg);
        } catch (Exception ex) {
            throw new Exception("ERROR: " + ex.getMessage());
        }
    }

    public String readLog() throws Exception {
        try {
            return this.reader.readLog();
        } catch (Exception ex) {
            throw new Exception("ERROR: " + ex.getMessage());
        }
    }
}
