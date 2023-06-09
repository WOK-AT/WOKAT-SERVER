package com.sopt.wokat.global.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.sopt.wokat.global.error.exception.BusinessException;

import jakarta.validation.ConstraintViolationException;

import static com.sopt.wokat.global.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.sopt.wokat.global.error.ErrorCode.METHOD_NOT_ALLOWED;
import static com.sopt.wokat.global.error.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getParameterName());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getConstraintViolations());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getBindingResult());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
		MissingServletRequestPartException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getRequestPartName());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(
		MissingRequestCookieException e) {
		final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE, e.getCookieName());
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		final ErrorResponse response = ErrorResponse.of(e);
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		final ErrorResponse response = ErrorResponse.of(HTTP_MESSAGE_NOT_READABLE);
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		final List<ErrorResponse.FieldError> errors = new ArrayList<>();
		errors.add(new ErrorResponse.FieldError("http method", e.getMethod(), METHOD_NOT_ALLOWED.getMessage()));
		final ErrorResponse response = ErrorResponse.of(HTTP_HEADER_INVALID, errors);
		return new ResponseEntity<>(response, BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse response = ErrorResponse.of(errorCode, e.getErrors());
		return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
	}

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		final ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
