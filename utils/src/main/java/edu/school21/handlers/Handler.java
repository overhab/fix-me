package edu.school21.handlers;

import java.nio.channels.AsynchronousSocketChannel;

public abstract class Handler<T> {

	protected int type;
	protected Handler<T> nextHandler;
	protected static int ERROR_CODE = 0;

	public Handler(int type) {
		this.type = type;
	}

	public void setNextHandler(Handler<T> nextHandler) {
		this.nextHandler = nextHandler;
	}

	public abstract String handle(T message, AsynchronousSocketChannel socket, int handler);
}
