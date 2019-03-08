package com.okayjam.bigdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOG =  LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        System.out.println("sout");
        LOG.info("Current Time: {}", System.currentTimeMillis());
        LOG.info("Curren Time: " + System.currentTimeMillis());
        LOG.info("Current Time: {}", System.currentTimeMillis());
        LOG.trace("trace log");
        LOG.warn("warn log");
        LOG.debug("debug log");
        LOG.info("info log");
        try{
            int a = 1/0;
        }catch (Exception e) {
            LOG.error("error log", e);
        }

    }
}
