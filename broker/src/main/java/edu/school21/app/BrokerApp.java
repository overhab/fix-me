package edu.school21.app;

import edu.school21.server.BrokerClient;

import java.io.IOException;

public class BrokerApp {

    public static void main(String[] args) {
        try {
            BrokerClient brokerClient = new BrokerClient();

            brokerClient.init(5000);
            if (args.length == 1) {
                brokerClient.autoSend(Integer.parseInt(args[0]));
            } else {
                brokerClient.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
