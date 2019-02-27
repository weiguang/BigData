package com.okayjam.bigdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger =  LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {

        logger.info("Current Time: {}", System.currentTimeMillis());
        logger.info("Current Time: " + System.currentTimeMillis());
        logger.info("Current Time: {}", System.currentTimeMillis());
        logger.trace("trace log");
        logger.warn("warn log");
        logger.debug("debug log");
        logger.info("info log");
        try{
            int a = 1/0;
        }catch (Exception e) {
            logger.error("error log", e);
        }

    }
}
