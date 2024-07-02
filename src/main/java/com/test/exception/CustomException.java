package com.test.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -7188080146479001711L;
	private final String errorMessage;
	private final Throwable cause;

	public CustomException(String errorMessage, Throwable cause) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.cause = cause;
	}
}