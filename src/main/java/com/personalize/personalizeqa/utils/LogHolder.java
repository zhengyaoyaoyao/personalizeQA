package com.personalize.personalizeqa.utils;

import com.personalize.personalizeqa.dto.LogRequestDuplicate;

public class LogHolder {
    private static final ThreadLocal<LogRequestDuplicate> logTl = new ThreadLocal<>();
    public static void saveLog(LogRequestDuplicate logRequestDuplicate){logTl.set(logRequestDuplicate);}
    public static LogRequestDuplicate getLogRequestDuplicate(){return logTl.get();}
    public static void removeLogRequestDuplicate(){logTl.remove();}
}
