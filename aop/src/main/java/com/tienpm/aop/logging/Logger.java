package com.tienpm.aop.logging;

import org.apache.logging.log4j.LogManager;

public class Logger {
    public static org.apache.logging.log4j.Logger json = LogManager.getLogger("JSON");
    public static org.apache.logging.log4j.Logger client = LogManager.getLogger("CLIENT");
    public static org.apache.logging.log4j.Logger machine = LogManager.getLogger("MACHINE");
    public static org.apache.logging.log4j.Logger endpoint = LogManager.getLogger("ENDPOINT");
    public static org.apache.logging.log4j.Logger info = LogManager.getLogger("INFO");
    public static org.apache.logging.log4j.Logger error = LogManager.getLogger("ERROR");
    public static org.apache.logging.log4j.Logger detailClient = LogManager.getLogger("DETAIL_CLIENT");
}
