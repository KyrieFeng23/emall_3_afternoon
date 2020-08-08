package com.emall_3_afternoon;

import com.emall_3_afternoon.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Emall3AfternoonApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                SpringApplication.run(Emall3AfternoonApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
        //SpringUtil.printBean();

        //SpringApplication.run(Emall3AfternoonApplication.class, args);
    }

}

