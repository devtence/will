package com.devtence.will.dev.commons.wrappers;

/**
 * Wrapper Class for the Google AppEngine Endpoints
 *
 * @author plessmann
 * @since 2016-03-17
 *
 */
public class BooleanWrapper {
    private boolean result;

    public BooleanWrapper(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
