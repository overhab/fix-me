package edu.school21.server;

import edu.school21.exceptions.InvalidFixMessage;
import edu.school21.handlers.Handler;
import edu.school21.models.Client;
import edu.school21.models.FixMessage;
import edu.school21.utils.FileLog;
import edu.school21.utils.Utils;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BrokerClient extends Client {

    private final Scanner scanner = new Scanner(System.in);

    public BrokerClient()  {
    }

    public void start() throws IOException {
        sendMessages();
    }

    public void autoSend(int count) throws IOException {
        String[] stockNames = {"Apple", "Gold", "Microsoft", "Tesla",
                "Visa", "Nvidia", "Intel", "Apple", "Amazon"};
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int quantity = random.nextInt(50);
            String stockName = stockNames[random.nextInt(stockNames.length)];
            String message = stockName + "#" + (random.nextInt(100) < 50 ? "buy" : "sell") + "#" + quantity + "#2.2#"
                    + (random.nextInt(100) < 50 ? "777000" : "777001") + "#" + id;

            try {
                fixMessage = new FixMessage(message);
                fixMessage.parseFixMessage();
            } catch (InputMismatchException | NumberFormatException | InvalidFixMessage e) {
                FileLog.LogMessage("[ERROR]: " + e.getMessage());
                break ;
            }
//            FileLog.LogMessage(id + " sending: " + fixMessage.getFixMessage());
            Utils.sendRequest(fixMessage.getFixMessage(), client);
            String response = Utils.getResponse(client);
            int code = processResponse(response);
            if (code == 2) {
                FileLog.LogMessage(id + " Executed");
            } else if (code == 8) {
                FileLog.LogMessage(id + " Rejected");
            } else {
                FileLog.LogMessage(id + " " + response);
            }
        }
        FileLog.close();
        client.close();
    }

    private void sendMessages() throws IOException {

        String input, response;

        while (true) {
            input = computeMessage();

            try {
                fixMessage = new FixMessage(input);
                fixMessage.parseFixMessage();
            } catch (InputMismatchException | NumberFormatException | InvalidFixMessage e) {
                System.out.println("[ERROR]: " + e.getMessage());
                continue;
            }

            Utils.sendRequest(fixMessage.getFixMessage(), client);
            if (input.equalsIgnoreCase("exit")) {
                requestId();
                break ;
            }

            response = Utils.getResponse(client);
            int code = processResponse(response);
            if (code == 2) {
                System.out.println("Executed");
            } else if (code == 8) {
                System.out.println("Rejected");
            } else {
                System.out.println(response);
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
        FixMessage fix;
        try {
            fix = new FixMessage(response, true);
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
        return fix.getStatus();
    }

    @Override
    public Handler initHandlers() {
        return null;
    }
}
