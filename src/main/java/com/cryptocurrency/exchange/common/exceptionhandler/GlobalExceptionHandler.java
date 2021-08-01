package com.cryptocurrency.exchange.common.exceptionhandler;

import com.cryptocurrency.exchange.errors.ApiConnectionException;
import com.cryptocurrency.exchange.errors.CryptocurrencyNotExistsException;
import com.cryptocurrency.exchange.errors.InvalidRequestBodyException;
import com.cryptocurrency.exchange.errors.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final String ERROR_OCCURRED = "error occurred";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorDTO apiError = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(errors)
                .build();

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(error))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(ERROR_OCCURRED))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundHandling(ResourceNotFoundException ex, WebRequest webRequest) {

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getLocalizedMessage())
                .errors(Arrays.asList(ERROR_OCCURRED))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler(ApiConnectionException.class)
    public ResponseEntity<Object> apiConnectionException(ApiConnectionException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .errors(Arrays.asList(ERROR_OCCURRED))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler(CryptocurrencyNotExistsException.class)
    public ResponseEntity<Object> cryptoCurrencyNotExistsException(CryptocurrencyNotExistsException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .errors(Arrays.asList(ERROR_OCCURRED))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Object> invalidRequestBodyException(InvalidRequestBodyException ex) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .errors(Arrays.asList(ERROR_OCCURRED))
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }
}
