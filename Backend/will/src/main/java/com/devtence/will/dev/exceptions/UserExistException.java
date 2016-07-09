package com.devtence.will.dev.exceptions;

/**
 * Exception created to manage not existing users on BaseControllers
 *
 * @author sorcerer
 * @since 2016-06-03
 * Created by sorcerer on 6/3/16.
 */
public class UserExistException extends Exception {

    public UserExistException(String message) {
        super(message);
    }
}
