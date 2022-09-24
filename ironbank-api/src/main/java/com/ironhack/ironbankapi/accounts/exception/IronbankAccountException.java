package com.ironhack.ironbankapi.accounts.exception;

public class IronbankAccountException extends Exception{

    public IronbankAccountException() {
    }

    public IronbankAccountException(String message) {
        super(message);
    }

    public IronbankAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public IronbankAccountException(Throwable cause) {
        super(cause);
    }

    public IronbankAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
