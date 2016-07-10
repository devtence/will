package com.devtence.will.dev.exceptions;

/**
 * Exception created  to manage missing entity fields on all the BaseModels classes.
 *
 * @author plessmann
 * @since 2016-03-16
 *
 */
public class MissingFieldException extends Exception {

    public MissingFieldException(String message) {
        super(message);
    }
}
