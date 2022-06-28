package edu.school21.app;

import edu.school21.config.MarketConfig;
import edu.school21.server.MarketClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(MarketConfig.class);
        MarketClient marketClient = context.getBean(MarketClient.class);

        marketClient.init(5001);
        marketClient.start();
    }
}
