package com.example.cms.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.cms.exception.UserAlreadyExistByEmailException;
import com.example.cms.exception.UserNotFoundByIdException;



@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	private ErrorStructure errorStructure;

	public ApplicationExceptionHandler(ErrorStructure errorStructure) {
		super();
		this.errorStructure = errorStructure;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> messages = new HashMap<>();
		ex.getAllErrors().forEach(error -> {
			FieldError fe = (FieldError) error;
			messages.put(fe.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(errorStructure.setMessage("Inavlid inputs")
				.setStatusCode(HttpStatus.BAD_GATEWAY.value()).setRootCause(messages));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> emailAlreadyExist(UserAlreadyExistByEmailException ex) {
		errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorStructure.setMessage(ex.getMessage());
		errorStructure.setRootCause("Email already registered with the same email");

		return new ResponseEntity<ErrorStructure>(errorStructure, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> userNotFoundByIdException(UserNotFoundByIdException ex){
		errorStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		errorStructure.setMessage(ex.getMessage());
		errorStructure.setRootCause("Please validate the userId and enter again cause no user is available with the given id");
		return new ResponseEntity<ErrorStructure>(errorStructure,HttpStatus.BAD_REQUEST);
				
				
				
				
				
	}
}
