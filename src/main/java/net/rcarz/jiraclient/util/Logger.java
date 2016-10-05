package net.rcarz.jiraclient.util;

import java.util.logging.Level;

/**
 * Created by Peter on 05.10.2016.
 */
public class Logger {
    Level DEFAULT_LEVEL = Level.WARNING;

    java.util.logging.Logger mLogger;

    protected Logger(java.util.logging.Logger logger){
        mLogger = logger;
        mLogger.setLevel(DEFAULT_LEVEL);

    }

    public static Logger getLogger(Class clazz){
        return new Logger(java.util.logging.Logger.getLogger(clazz.getName()));
    }

    public void log(Level level, String msg) {
        mLogger.log(level, msg);
    }
}
