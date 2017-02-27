package com.aws.test;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class Log
{
    private static Logger logger = Logger.getLogger("Logger");
    private static FileHandler fh;
    
    static
    {
        try
        {
			fh = new FileHandler("TestLogFile.log");
		} 
        catch (SecurityException | IOException e)
        {
			e.printStackTrace();
		}
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);       
    }
    
    public static void info(String message)
    {
    	logger.info(message);
	}
 
    public static void info(String className, String message)
    {
    	logger.info("[" + className + "]: " + message);
	}
}