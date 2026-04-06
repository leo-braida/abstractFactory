package com.abstractFactory;

public interface LogAbstractFactory {
    public LogReader createReader();
    public LogWriter createWriter();
}
