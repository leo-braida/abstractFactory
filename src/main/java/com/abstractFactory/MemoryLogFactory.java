package com.abstractFactory;

public class MemoryLogFactory implements LogAbstractFactory {
    @Override
    public LogReader createReader() {
        return new MemoryLogReader();
    }

    @Override
    public LogWriter createWriter() {
        return new MemoryLogWriter();
    }
}
