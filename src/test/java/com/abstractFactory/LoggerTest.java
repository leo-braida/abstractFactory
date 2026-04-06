package com.abstractFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class LoggerTest {
    @Test
    void shouldReturnLog() {
        LogAbstractFactory factory = new MemoryLogFactory();
        Logger logger = new Logger(factory);

        String testLog = "Test log";
        logger.setLog(testLog);
        try {
            assertEquals(testLog, logger.readLog(), "Should return the setted log.");
        } catch (Exception ex) {
            fail();
        }
    }

    // memoryLog

    @Test
    void shouldWriteMemoryLogWithMsg() {
        LogAbstractFactory factory = new MemoryLogFactory();
        Logger logger = new Logger(factory);

        Instant testTime = Instant.parse("2024-01-01T10:00:00Z");
        String origin = "TEST";
        String msg = "Test message";
        String fullMsg = testTime + " - [" + origin + "]: " + msg;
        String expectedReturn = "Successfully wrote log.";

        try (MockedStatic<Instant> mockInstant = mockStatic(Instant.class)) {
            mockInstant.when(Instant::now).thenReturn(testTime);

            String returnMsg = logger.writeLog(origin, msg);
            assertEquals(expectedReturn, returnMsg, "Should return the expected message.");
            assertEquals(fullMsg, logger.readLog(), "Log should return only the passed message.");
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void shouldAppendMemoryLogWithMsg() {
        LogAbstractFactory factory = new MemoryLogFactory();
        Logger logger = new Logger(factory);

        Instant testTime1 = Instant.parse("2025-01-01T10:00:00Z");
        String origin1 = "TEST";
        String msg1 = "First test msg";
        String fullMsg1 = testTime1 + " - [" + origin1 + "]: " + msg1;

        Instant testTime2 = Instant.parse("2026-01-01T10:00:00Z");
        String origin2 = "TEST2";
        String msg2 = "Second test msg";
        String fullMsg2 = testTime2 + " - [" + origin2 + "]: " + msg2;

        String expectedLog = fullMsg1 + "\n" + fullMsg2;
        String expectedReturn = "Successfully wrote log.";

        try (MockedStatic<Instant> mockInstant = mockStatic(Instant.class)) {
            mockInstant.when(Instant::now).thenReturn(testTime1, testTime2);

            logger.writeLog(origin1, msg1);
            String returnMsg = logger.writeLog(origin2, msg2);

            assertEquals(expectedReturn, returnMsg, "Should return the expected message.");
            assertEquals(expectedLog, logger.readLog(), "Log should return both test messages.");
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void shouldSetMemoryLog() {
        LogAbstractFactory factory = new MemoryLogFactory();
        Logger logger = new Logger(factory);

        String testLog = "Test log";

        logger.setLog(testLog);

        assertEquals(testLog, logger.getLog().toString(), "Should set log to passed message.");
    }

    // fileLog

    @Test
    void shouldThrowExceptionForEmptyLogFilePath(){
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        String testOrigin = "TEST";
        String testMsg = "Test";

        String expectedError = "ERROR: Unable to write empty log to file.";


        Exception ex = assertThrows(Exception.class, () -> {
            logger.writeLog(testOrigin, testMsg);
        });

        assertEquals(expectedError, ex.getMessage(), "Should return error for file path not being setted.");

    }

    @Test
    void shouldThrowExceptionForInvalidLogFilePath(@TempDir Path tempDir) {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        Path invalidPath = tempDir.resolve("~/error");
        logger.setLog(invalidPath.toString());

        Exception ex = assertThrows(Exception.class, () -> {
            String testOrigin = "TEST";
            String testMsg = "test";
            logger.writeLog(testOrigin, testMsg);
        });

        String expectedError = "ERROR: Cannot write log to " + invalidPath.toString();
        assertEquals(expectedError, ex.getMessage(), "Should throw error for invalid log file path.");
    }

    @Test
    void shouldWriteLogFile(@TempDir Path tempDir) {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        Path testPath = tempDir.resolve("test.log");
        logger.setLog(testPath.toString());

        Instant testTime = Instant.parse("2026-01-01T10:00:00Z");

        try (MockedStatic<Instant> mockInstant = mockStatic(Instant.class)) {
            mockInstant.when(Instant::now).thenReturn(testTime);
            String testOrigin = "TEST";
            String testMsg = "test";

            String functionReturn = logger.writeLog(testOrigin, testMsg);

            String expectedReturn = "Successfully wrote log in: " + testPath.toString();
            String expectedLog = testTime + " - [" + testOrigin + "]: " + testMsg;

            assertEquals(expectedReturn, functionReturn, "Should return the expected message for successfull case.");
            assertEquals(expectedLog, logger.readLog(), "Should return the expected log.");
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void shouldAppendNewLineToLogFile(@TempDir Path tempDir) {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        Path testPath = tempDir.resolve("test.log");
        logger.setLog(testPath.toString());

        Instant testTime = Instant.parse("2026-01-01T10:00:00Z");
        Instant testTime2 = Instant.parse("2026-01-02T10:00:00Z");

        try (MockedStatic<Instant> mockInstant = mockStatic(Instant.class)) {

            mockInstant.when(Instant::now).thenReturn(testTime, testTime2);
            String testOrigin = "TEST";
            String testMsg = "test";

            String testOrigin2 = "ERROR";
            String testMsg2 = "error msg";

            logger.writeLog(testOrigin, testMsg);
            String functionReturn = logger.writeLog(testOrigin2, testMsg2);
            String expectedReturn = "Successfully wrote log in: " + testPath.toString();
            String expectedLog = testTime + " - [" + testOrigin + "]: " + testMsg + "\n" + testTime2 + " - [" + testOrigin2 + "]: " + testMsg2;

            assertEquals(expectedReturn, functionReturn, "Should return the expected message for successfull case.");
            assertEquals(expectedLog, logger.readLog(), "Should return the expected log.");
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    void shouldReturnErrorForNullLogPath() {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        Exception ex = assertThrows(Exception.class, () -> {
            logger.readLog();
        });

        String expectedError = "ERROR: Couldn't find log.";
        assertEquals(expectedError, ex.getMessage(), "Should throw the expected error for missing log path.");
    }

    @Test
    void shouldReturnErrorForNonexistentFile(@TempDir Path tempDir) {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        Path testPath = tempDir.resolve("test.log");
        logger.setLog(testPath.toString());

        Exception ex = assertThrows(Exception.class, () -> {
            logger.readLog();
        });

        String expectedError = "ERROR: Couldn't find log.";
        assertEquals(expectedError, ex.getMessage(), "Should throw the expected error for nonexistent file.");
    }

    @Test
    void shouldReturnErrorForUnreadableLogFile(@TempDir Path tempDir) {
        LogAbstractFactory factory = new FileLogFactory();
        Logger logger = new Logger(factory);

        logger.setLog(tempDir.toString());

        Exception ex = assertThrows(Exception.class, () -> {
            logger.readLog();
        });

        String expectedError = "ERROR: Couldn't read the log file.";
        assertEquals(expectedError, ex.getMessage(), "Should return error for unreadable log file while trying to read a directory.");
    }
}
