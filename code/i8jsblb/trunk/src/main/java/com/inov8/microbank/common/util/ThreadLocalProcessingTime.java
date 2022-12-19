package com.inov8.microbank.common.util;

public class ThreadLocalProcessingTime {

    private static ThreadLocal<StringBuilder> processingTime = new ThreadLocal<>();

    private ThreadLocalProcessingTime(){
    }

    public static void append(long startTime, String taskName, String taskId) {
        long endTime = System.currentTimeMillis();
        double timeSpent = (endTime - startTime)/1000.0;

        if(taskName != null && taskName.length() < 30)
            taskName = String.format("%-30s" , taskName);

        if(taskId != null && taskId.length() < 30)
            taskId = String.format("%-30s" , taskId);

        getProcessingTime().append("\n=> C: " + taskName + " A: " + taskId + " T: " + timeSpent + " sec");
    }

    private static StringBuilder getProcessingTime(){
        StringBuilder str = processingTime.get();
        if(str == null) {
            str = new StringBuilder();
            processingTime.set(str);
        }

        return str;
    }

    public static void remove(){
        processingTime.remove();
    }

    public static String getOutput(){
        return getProcessingTime().toString() + "\n";
    }
}