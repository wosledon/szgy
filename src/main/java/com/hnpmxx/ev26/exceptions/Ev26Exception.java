package com.hnpmxx.ev26.exceptions;

public class Ev26Exception extends Exception {
    public Ev26ErrorCode errorCode;

    public Ev26Exception(Ev26ErrorCode errorCode){
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public Ev26Exception(Ev26ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public Ev26Exception(Ev26ErrorCode errorCode, Exception ex){
        super(ex.getMessage(), ex);
        this.errorCode = errorCode;
    }
    public Ev26Exception(Ev26ErrorCode errorCode, String message, Exception ex){
        super(message, ex);
        this.errorCode = errorCode;
    }
}
