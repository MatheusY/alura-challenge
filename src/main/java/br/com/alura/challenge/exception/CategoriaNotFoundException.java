package br.com.alura.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CategoriaNotFoundException extends RecordNotFoundException {

	private static final long serialVersionUID = 2209425009642423213L;
	private static final String CATEGORIA_NAO_ENCONTRADA = "Categoria não encontrada!";

	public CategoriaNotFoundException() {
		super(CATEGORIA_NAO_ENCONTRADA);
	}

}
