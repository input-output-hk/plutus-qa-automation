package io.iohk.utils;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {

    static{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        System.setProperty("current.date.time", dateFormat.format(new Date()));
        System.setProperty("outputLoggingDir", System.getProperty("user.dir") + "\\logs");
    }

    //Initialize Log4j instance
    private static Logger Log = Logger.getLogger(io.iohk.utils.Log.class.getName());

    //Info Level Logs
    public static void info (String message) {
        Log.info(message);
    }

    //Warn Level Logs
    public static void warn (String message) {
        Log.warn(message);
    }

    //Error Level Logs
    public static void error (String message) {
        Log.error(message);
    }

    //Fatal Level Logs
    public static void fatal (String message) {
        Log.fatal(message);
    }

    //Debug Level Logs
    public static void debug (String message) {
        Log.debug(message);
    }
}
