package edu.school21.exceptions;

public class ConnectionUnavailable extends RuntimeException{
	public ConnectionUnavailable(String message) {
		super(message);
	}
}
