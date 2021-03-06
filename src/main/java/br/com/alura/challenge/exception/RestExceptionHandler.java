package br.com.alura.challenge.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.alura.challenge.domain.entity.FieldError;
import br.com.alura.challenge.domain.vo.ErrorResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CAMPOS_INVALIDOS = "Campos inválidos!";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		List<FieldError> fieldErrors = new ArrayList<>();

		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			fieldErrors.add(new FieldError(error.getObjectName(), error.getDefaultMessage()));
		}

		for (org.springframework.validation.FieldError error : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.add(new FieldError(error.getField(), error.getDefaultMessage()));
		}
		ErrorResponse erro = new ErrorResponse(CAMPOS_INVALIDOS, status.getReasonPhrase(), fieldErrors);
		return new ResponseEntity<>(erro, status);
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRecordNotFound(final RecordNotFoundException ex, final WebRequest request) {
		return createResponseEntity(ex, request, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<ErrorResponse> createResponseEntity(final Exception exception, final WebRequest request, final HttpStatus status) {
		ErrorResponse error = ErrorResponse.builder() //
				.message(exception.getMessage()) //
				.details(request.getDescription(false)) //
				.build();

		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, WebRequest request) {
		ErrorResponse error = ErrorResponse.builder() //
				.message(ex.getMessage()) //
				.details(request.getDescription(false)) //
				.build();

		return new ResponseEntity<>(error, ex.getStatus());
	}

}
