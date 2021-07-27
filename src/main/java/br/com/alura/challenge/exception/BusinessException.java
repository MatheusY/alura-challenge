package br.com.alura.challenge.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends Exception {

	private static final long serialVersionUID = 3386266501023787248L;

	private final HttpStatus status;

	public BusinessException(final String message, final HttpStatus status) {
		super(message);
		this.status = status;

	}
}
