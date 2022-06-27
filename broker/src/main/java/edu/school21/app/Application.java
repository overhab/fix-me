package edu.school21.app;

import edu.school21.server.BrokerClient;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        try {
            BrokerClient brokerClient = new BrokerClient();

            brokerClient.init(5000);
            brokerClient.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
