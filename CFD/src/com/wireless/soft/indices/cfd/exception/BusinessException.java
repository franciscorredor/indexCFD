package com.wireless.soft.indices.cfd.exception;

/**
 * @author 	Francisco Corredor
 * @since  	01Dec2015
 * @version	1.0  
 */
public class BusinessException extends Exception {

    // ////////////////////////////////////////////////////////////////////////
    // Serial Version UID
    // ////////////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = -5117519543098752766L;

    // ////////////////////////////////////////////////////////////////////////
    // Constructor de la clase
    // ////////////////////////////////////////////////////////////////////////
    /**
     * Constructor de la clase
     * @param message
     */
    public BusinessException(String message) {
	super(message);
    }

    /**
     * Constructor de la clase
     * @param message
     * @param cause
     */
    public BusinessException(String message, Throwable cause) {
	super(message, cause);
    }

}
