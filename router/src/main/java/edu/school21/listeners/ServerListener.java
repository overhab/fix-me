package edu.school21.listeners;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Scanner;

@Component
@Scope("prototype")
public class ServerListener implements Runnable {

    private final Scanner scanner = new Scanner(System.in);
    private final AsynchronousServerSocketChannel marketSocket;
    private final AsynchronousServerSocketChannel brokerSocket;

    public ServerListener(AsynchronousServerSocketChannel marketSocket,
                          AsynchronousServerSocketChannel brokerSocket) {
        this.marketSocket = marketSocket;
        this.brokerSocket = brokerSocket;
    }

    @Override
    public void run() {
        String input;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("shutdown") || input.equals("stop")) {
                try {
                    System.out.println("closing...");
                    marketSocket.close();
                    brokerSocket.close();
                    return ;
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    return ;
                }
            }
        }
    }


}
