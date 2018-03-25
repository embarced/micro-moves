package org.flexess.games.service;

public class IllegalMoveException extends RuntimeException {
    IllegalMoveException(String msg) {
        super(msg);
    }

    IllegalMoveException(String msg, Throwable t) {
        super(msg, t);
    }
}
