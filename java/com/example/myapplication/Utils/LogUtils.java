package com.example.myapplication.Utils;

import android.util.Log;

/**
 * Log printing tool
 */

public class LogUtils {

    public static boolean debug = true;
    public static final String START = "START----->";
    public static final String END = "END----->";

    // Information level
    public static void i(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.i(newTag, msg);
    }

    // Debug level
    public static void d(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.d(newTag, msg);
    }

    // Warning level
    public static void w(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.w(newTag, msg);
    }

    // Detail
    public static void v(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.v(newTag, msg);
    }

    // Error level
    public static void e(Object tag, String msg) {
        if (!debug) {
            return;
        }
        String newTag = getNewTag(tag);
        Log.e(newTag, msg);
    }

    private static String getNewTag(Object tag) {
        String newTag = "";

        if (tag instanceof String) {
            newTag = (String) tag;
        } else if (tag instanceof Class) {
            newTag = ((Class) tag).getSimpleName();
        } else {
            newTag = tag.getClass().getSimpleName();
        }
        return START +newTag+ END;
    }

    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static int line(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return -1; //
        return trace[0].getLineNumber();
    }
    public static String fun(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getMethodName();
    }
    public static String filename(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getFileName();
    }
    public static String funAndLine(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return ""; //
        return  trace[0].getMethodName()+"|"+trace[0].getLineNumber()+"|";
    }
}
