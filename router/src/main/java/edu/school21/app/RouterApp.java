package edu.school21.app;

import edu.school21.config.FixMeApplicationConfig;
import edu.school21.server.RouterServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class RouterApp {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(FixMeApplicationConfig.class);

        RouterServer router = context.getBean(RouterServer.class);

        try {
            router.start();
        } catch (IOException exception) {
            System.out.println("[ERROR]:" + exception.getMessage());
        }

    }
}
