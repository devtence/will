package com.devtence.will.dev.exceptions;

/**
 * A custom Exception used to manage not existing users
 *
 * Created by sorcerer on 6/3/16.
 */
public class UserExistException extends Exception {

    public UserExistException(String message) {
        super(message);
    }
}
