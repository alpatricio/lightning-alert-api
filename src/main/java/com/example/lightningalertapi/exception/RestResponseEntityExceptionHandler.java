package com.example.lightningalertapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = { ServiceException.class })
    protected ResponseEntity<Object> clientException(Exception ex, WebRequest request)
    {
        LOG.error("Error encountered", ex);
        return new ResponseEntity<>(new Error(ex.getMessage()), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { ResourceAccessException.class })
    protected ResponseEntity<Object> resourceAccessException(Exception ex, WebRequest request)
    {
        LOG.error("Error encountered", ex);
        return new ResponseEntity<>(new Error("Problem accessing "+ex.getCause().getLocalizedMessage()), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> validationException(Exception ex, WebRequest request) {
        LOG.error("Error encountered", ex);
        return new ResponseEntity<>(new Error(ex.getMessage()), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> generalException(Exception ex, WebRequest request)
    {
        LOG.error("Error encountered", ex);
        return new ResponseEntity<>(new Error("General Exception"), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
