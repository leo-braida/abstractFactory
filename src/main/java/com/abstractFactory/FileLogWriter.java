package com.abstractFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLogWriter implements LogWriter {
    @Override
    public String writeLog(StringBuilder log,String msg) throws Exception {
        if (log.isEmpty()) {
            throw new Exception("Unable to write empty log to file.");
        }

        Path path = Paths.get(log.toString());
        msg += "\n";

        try {
            Files.write(path, msg.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return "Successfully wrote log in: " + log;
        } catch (IOException ex) {
            throw new Exception("Cannot write log to " + log);
        }
    }
}