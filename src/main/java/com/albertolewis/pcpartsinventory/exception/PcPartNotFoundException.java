package com.albertolewis.pcpartsinventory.exception;


public class PcPartNotFoundException extends RuntimeException {

    public PcPartNotFoundException(String message) {
        super(message);
    }
}
