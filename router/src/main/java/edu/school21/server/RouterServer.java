package edu.school21.server;

import edu.school21.handlers.*;
import edu.school21.listeners.ServerListener;
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

            Handler<String> handler = initHandlers(routingTable);

            brokerServer.accept(null, new RouterConnectionHandler(routingTable, brokerServer, handler));
            marketServer.accept(null, new RouterConnectionHandler(routingTable, marketServer, handler));

            listener.join();

        } catch (IOException | InterruptedException ex) {
            marketServer.close();
            brokerServer.close();
            ex.printStackTrace(System.out);
        }

        brokerServer.close();
        marketServer.close();
    }

    public Handler<String> initHandlers(Map<String, AsynchronousSocketChannel> routingTable) {
        Handler<String> requestHandler = new RequestHandler(HandlerType.REQUEST, routingTable);
        Handler<String> forwardHandler = new ForwardHandler(HandlerType.FORWARD, routingTable);
        Handler<String> validationHandler = new ValidationHandler(HandlerType.VALIDATION, routingTable);
        Handler<String> errorHandler = new ErrorHandler(HandlerType.ERROR, routingTable);
        Handler<String> disconnectHandler = new DisconnectHandler(HandlerType.DISCONNECT, routingTable);

        requestHandler.setNextHandler(validationHandler);
        validationHandler.setNextHandler(forwardHandler);
        forwardHandler.setNextHandler(errorHandler);
        errorHandler.setNextHandler(disconnectHandler);

        return requestHandler;
    }
}
