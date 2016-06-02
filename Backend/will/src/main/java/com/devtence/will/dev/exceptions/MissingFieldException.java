package com.devtence.will.dev.exceptions;

/**
 * A custom Exception used to manage missing entity fields
 * Created by plessmann on 16/03/16.
 */
public class MissingFieldException extends Exception {

	public MissingFieldException(String message) {
		super(message);
	}
}
