package com.testcase.conference.exceptions;

/*
TODO replace with more informative exceptions
 */
public class MySpringException extends RuntimeException {


    public MySpringException(String message, Exception exception) {
        super(message, exception);
    }

    public MySpringException(String message) {
        super(message);
    }
}
