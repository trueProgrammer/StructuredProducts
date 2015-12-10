package com.structuredproducts.sevices;

/**
 * Created by Vlad on 10.12.2015.
 */
public class ServiceException extends Exception {

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Exception e) {
        super(msg,e);
    }

}
