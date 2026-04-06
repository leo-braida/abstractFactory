package com.abstractFactory;

public class FileLogFactory implements LogAbstractFactory {
    @Override
    public LogReader createReader() {
        return new FileLogReader();
    }

    @Override
    public LogWriter createWriter() {
        return new FileLogWriter();
    }
}
