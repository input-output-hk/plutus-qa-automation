package io.iohk.utils;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/*
* This appender will create a separate log file based on "current" thread id
*/
public class GroupedLoggingAppender extends AppenderSkeleton {
   private final ConcurrentHashMap<Long, BufferedWriter> tid2file = new ConcurrentHashMap<Long, BufferedWriter>();

   private final String outputFile;

    public GroupedLoggingAppender() {
       String outdir = System.getProperty("outputLoggingDir");
       String currentTime = System.getProperty("current.date.time");

       if (!outdir.endsWith("/"))
           outdir += "/";
       outputFile = outdir + currentTime;
   }

   @Override
   public void close() {
   }

   @Override
   public void append(LoggingEvent event) {
       if (outputFile == null)
           return;
       try {
           long tid = Thread.currentThread().getId();
           BufferedWriter fw = tid2file.get(tid);
           if (fw == null) {
               fw = new BufferedWriter(new FileWriter(getFileNameFromThreadID(tid)));
               tid2file.put(tid, fw);
           }
           fw.write(event.getMessage().toString());
           fw.write("\n");
           fw.flush();
       } catch (IOException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
   }

   private String getFileNameFromThreadID(long tid) {
       return String.format("%s_thread_output_%04d%s", outputFile, tid, ".threadlog.log");
   }

   @Override
   public boolean requiresLayout() {
       return true;
   }
}

