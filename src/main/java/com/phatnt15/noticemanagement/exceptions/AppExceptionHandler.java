package com.phatnt15.noticemanagement.exceptions;

import com.phatnt15.noticemanagement.dtos.GenericResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * The type App exception handler.
 */
@RestControllerAdvice
public class AppExceptionHandler {

    private static final List<String> CUSTOM_ERROR_CODES = Arrays.asList("ValidDateRange");

    /**
     * Method argument not valid exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<HashMap<String, String>> methodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        GenericResponse<HashMap<String, String>> response = new GenericResponse<>();
        HashMap<String, String> errors = new HashMap<>();

        // field level errors
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        // class level errors
        ex.getBindingResult().getAllErrors().forEach(err -> {
            if (CUSTOM_ERROR_CODES.contains(err.getCode())) {
                errors.put(err.getCode(), err.getDefaultMessage());
            }
        });
        response.setMessage(errors);

        return response;
    }


    /**
     * Method argument type mismatch exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<String> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }

    /**
     * Http message not readable exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<String> httpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }


    /**
     * Entity not found exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public GenericResponse<String> entityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }

    /**
     * User not foun exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<String> userNotFounException(
            UsernameNotFoundException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }


    /**
     * Notice view file exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(NoticeViewFileException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public GenericResponse<String> noticeViewFileException(
            NoticeViewFileException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }

    /**
     * Access denied exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public GenericResponse<String> accessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage("access denied");

        return response;
    }

    /**
     * App generic exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(AppGenericException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<String> appGenericException(
            Exception ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }

    /**
     * User already registered exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(UserAlreadyRegisteredException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public GenericResponse<String> userAlreadyRegisteredException(
            UserAlreadyRegisteredException ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage(ex.getMessage());

        return response;
    }

    /**
     * Unknown exception generic response.
     *
     * @param ex      the ex
     * @param request the request
     * @return the generic response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<String> unknownException(
            Exception ex,
            HttpServletRequest request) {
        GenericResponse<String> response = new GenericResponse<>();
        response.setMessage("An exception was occurred, please contact IT team.");

        return response;
    }
}
