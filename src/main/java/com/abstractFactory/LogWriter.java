package com.abstractFactory;

import java.io.IOException;

public interface LogWriter {
    public String writeLog(StringBuilder log, String msg) throws Exception;
}
