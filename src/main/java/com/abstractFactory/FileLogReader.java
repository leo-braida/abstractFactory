package com.abstractFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLogReader implements LogReader{
    Path logPath;

    @Override
    public void setLog(String log) {
        this.logPath = Paths.get(log);
    }

    @Override
    public StringBuilder getLog() {
        if (this.logPath == null) return new StringBuilder();
        return new StringBuilder(this.logPath.toString());
    }

    @Override
    public String readLog() throws Exception {
        if (this.logPath == null || !Files.exists(this.logPath)) {
            throw new Exception("Couldn't find log.");
        }

        try {
            Stream<String> lines = Files.lines(this.logPath);
            String fullLog = lines.collect(Collectors.joining("\n"));
            return fullLog;
        } catch (Exception ex) {
            throw new Exception("Couldn't read the log file.");
        }
    }
}