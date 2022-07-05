package edu.school21.server;

import edu.school21.handlers.*;
import edu.school21.listeners.ServerListener;
import edu.school21.utils.FileLog;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class RouterServer {

    private AsynchronousServerSocketChannel marketServer;
    private AsynchronousServerSocketChannel brokerServer;
    private final Map<String, AsynchronousSocketChannel> routingTable = new HashMap<>();
    private final Map<String, String> ongoingRequests = new HashMap<>();

    public RouterServer() {
    }

    public void start() throws IOException {

        try {
            System.out.println("Starting server...");

            brokerServer = AsynchronousServerSocketChannel.open();
            brokerServer.bind(new InetSocketAddress("localhost", 5000));

            marketServer = AsynchronousServerSocketChannel.open();
            marketServer.bind(new InetSocketAddress("localhost",5001));

            Thread listener = new Thread(new ServerListener(marketServer, brokerServer));
            listener.start();

            System.out.println("listening on ports 5000 and 5001");

            Handler<String> handler = initHandlers();

            brokerServer.accept(null, new RouterConnectionHandler(routingTable, brokerServer, handler));
            marketServer.accept(null, new RouterConnectionHandler(routingTable, marketServer, handler));

            listener.join();

        } catch (IOException | InterruptedException ex) {
            marketServer.close();
            brokerServer.close();
            FileLog.close();
            ex.printStackTrace(System.out);
        }

        FileLog.close();
        brokerServer.close();
        marketServer.close();
    }

    public Handler<String> initHandlers() {
        Handler<String> acceptRequestHandler = new AcceptRequestHandler(HandlerType.REQUEST, routingTable, ongoingRequests);
        Handler<String> fixMessageHandler = new FixMessageHandler(HandlerType.MESSAGE, routingTable, ongoingRequests);
        Handler<String> errorHandler = new ErrorHandler(HandlerType.ERROR, routingTable, ongoingRequests);
        Handler<String> disconnectHandler = new DisconnectHandler(HandlerType.DISCONNECT, routingTable, ongoingRequests);

        acceptRequestHandler.setNextHandler(fixMessageHandler);
        fixMessageHandler.setNextHandler(errorHandler);
        errorHandler.setNextHandler(disconnectHandler);

        return acceptRequestHandler;
    }
}
