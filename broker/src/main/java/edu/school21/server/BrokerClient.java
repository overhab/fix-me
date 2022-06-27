package edu.school21.server;

import edu.school21.models.Client;
import edu.school21.models.FixMessage;
import edu.school21.utils.Utils;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BrokerClient extends Client {

    private final Scanner scanner = new Scanner(System.in);

    public BrokerClient()  {
    }

    public void start() throws IOException {
        sendMessages();
    }

    private void sendMessages() throws IOException {

        String input, response;

        while (true) {
            input = computeMessage();

            try {
                fixMessage = new FixMessage(input);
                fixMessage.parseFixMessage();
            } catch (InputMismatchException e) {
                System.out.println("[ERROR]: " + e.getMessage());
                continue;
            }

            Utils.sendRequest(fixMessage.getFixMessage(), client);
            if (input.equalsIgnoreCase("exit")) {
                requestId();
                break ;
            }

            response = Utils.getResponse(client);
            if (processResponse(response) == 8) {
                System.out.println("Executed");
            } else {
                System.out.println("Rejected");
            }
        }

        client.close();
    }

    private String computeMessage() {
        StringBuilder sb = new StringBuilder();

        System.out.println("What item you want to buy/sell?");
        sb.append(scanner.nextLine());
        System.out.println("What is the type of transaction (buy or sell)?");
        sb.append("#" + scanner.nextLine());
        System.out.println("What is the quantity?");
        sb.append("#" + scanner.nextLine());
        System.out.println("What is the price?");
        sb.append("#" + scanner.nextLine());
        System.out.println("What is the id of the market?");
        sb.append("#" + scanner.nextLine());
        sb.append("#" + id);

        return sb.toString();
    }

    private int processResponse(String response) {
        FixMessage fix = new FixMessage(response, true);

        return fix.getStatus();
    }
}
