package org.flexess.games.rulesclient;

public class MoveValidationNotPossibleException extends Exception{

    public MoveValidationNotPossibleException(String msg) {
        super(msg);
    }

    public MoveValidationNotPossibleException(String msg, Throwable t) {
        super(msg, t);
    }

}
