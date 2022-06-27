package edu.school21.server;

import edu.school21.listeners.ServerListener;
import edu.school21.utils.Utils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class RouterServer {

    private AsynchronousServerSocketChannel marketServer;
    private AsynchronousServerSocketChannel brokerServer;
    private final Map<String, AsynchronousSocketChannel> routingTable = new HashMap<>();

    public RouterServer() {
    }

    public void start() throws IOException {

        try {
            System.out.println("Starting server...");

            brokerServer = AsynchronousServerSocketChannel.open();
            brokerServer.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            brokerServer.bind(new InetSocketAddress("localhost", 5000));

            marketServer = AsynchronousServerSocketChannel.open();
            marketServer.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            marketServer.bind(new InetSocketAddress("localhost",5001));

            Thread listener = new Thread(new ServerListener(marketServer, brokerServer));
            listener.start();

            System.out.println("listening on ports 5000 and 5001");

            brokerServer.accept(null, new RouterConnectionHandler(routingTable, brokerServer));
            marketServer.accept(null, new RouterConnectionHandler(routingTable, marketServer));

            listener.join();
//            listening();

        } catch (IOException | InterruptedException ex) {
            marketServer.close();
            brokerServer.close();
            ex.printStackTrace(System.out);
        }

        brokerServer.close();
        marketServer.close();
    }

    /*private void listening() throws IOException {
        System.out.println("Starting listening....");
        while (true) {
            Future<AsynchronousSocketChannel> acceptMarket = marketServer.accept();
            Future<AsynchronousSocketChannel> acceptBroker = brokerServer.accept();
            try {
                AsynchronousSocketChannel client = acceptMarket.get();
                Utils.sendRequest(String.valueOf(id), client);
                routingTable.put(id, client);
                System.out.println("Connected " + id);
                Worker worker = new Worker(routingTable, client, id);
                id++;
                *//*Callable<String> worker = new Callable<String>() {
                    @Override
                    public String call() throws Exception {

                        Future<Integer> readResult;
                        String response;
                        while (client.isOpen()) {
                            ByteBuffer buffer = ByteBuffer.allocate(50);
                            readResult = client.read(buffer);
                            readResult.get();
                            response = new String(buffer.array()).trim();

                            if (response.equalsIgnoreCase("exit")) {
                                globalMessage(client.getRemoteAddress().toString());
                                client.close();
                                return "exit";
                            }

                            Utils.sendRequest(response, client);
                            buffer.clear();
                        }

                        client.close();
                        return "asd";
                    }
                };*//*
                taskExecutor.submit(worker);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return ;
            }
        }
    }*/
}
