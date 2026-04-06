package com.abstractFactory;

public interface LogReader {
    public void setLog(String log);
    public StringBuilder getLog();
    public String readLog() throws Exception;
}
