package com.devtence.will.dev.exceptions;

/**
 * Exception created to manage not existing users on all BaseControllers classes.
 *
 * @author sorcerer
 * @since 2016-06-03
 *
 */
public class UserExistException extends Exception {

    public UserExistException(String message) {
        super(message);
    }
}
