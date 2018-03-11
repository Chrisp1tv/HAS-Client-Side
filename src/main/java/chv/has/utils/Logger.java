package chv.has.utils;

import chv.has.HAS;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * @author Christopher Anciaux
 */
abstract public class Logger {
    protected static final String LOGS_PATH = "%h/.has/logs.log";

    protected static java.util.logging.Logger logger;

    public static void log(String message) {
        Logger.log(Level.INFO, message);
    }

    public static void logException(Exception exception) {
        Logger.log(Level.WARNING, exception.toString());
    }

    public static void log(Level level, String message) {
        if (null == Logger.logger) {
            Logger.setUpLogger();
        }

        Logger.logger.log(level, message);
    }

    protected static void setUpLogger() {
        Logger.logger = java.util.logging.Logger.getLogger(HAS.class.getName());
        Logger.logger.setUseParentHandlers(false);

        try {
            Handler handler = new FileHandler(Logger.LOGS_PATH, true);
            handler.setFormatter(new SimpleFormatter());

            Logger.logger.addHandler(handler);
        } catch (IOException ignored) {
        }
    }
}
