package com.cms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CmsApplication {

    private static final Logger logger = LogManager.getLogger(CmsApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Contact Management Application...");
        SpringApplication.run(CmsApplication.class, args);
        logger.info("Contact Management Application started successfully!!!!");
    }

}
