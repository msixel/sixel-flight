package org.sixel.flight.rest.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.sixel.flight.core.exception.CoreException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author marcos
 *
 */
@ControllerAdvice
class GlobalExceptionHandler {
	
	/**
	 * 
	 */
	private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName()); 
	
    /**
     * 
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CoreException.class)
    public @ResponseBody Info handleInvalidRouteFormatException(final HttpServletRequest req, final CoreException ce) {
    	LOGGER.log(Level.SEVERE, ce.getMessage(), ce);
    	return new Info(req.getRequestURL().toString(), ce);
    }
    
    /**
     * @author marcos
     *
     */
    class Info {
    	public final Date timestamp;
        public final String url;
        public final String exception;
        public final String message;

        public Info(String url, Exception ex) {
        	this.timestamp = new Date();
            this.url = url;
            this.exception = ex.getClass().getSimpleName();
            this.message = ex.getLocalizedMessage();
        }
    }
}