package com.app.exception;

import java.time.LocalDateTime;

public class ErrorDetails {

	private LocalDateTime timestamp;
	private String error;
	private String message;
	private String path;

	public ErrorDetails(LocalDateTime timestamp, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.error = error;
		this.path = path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public String getError() {
		return error;
	}

}
