package br.com.alura.challenge.exception;

public abstract class RecordNotFoundException extends RuntimeException {

	private static final String REGISTRO_NAO_ENCONTRADO = "Registro não encontrado!";
	private static final long serialVersionUID = 3403307390252037860L;

	public RecordNotFoundException() {
		super(REGISTRO_NAO_ENCONTRADO);
	}

	public RecordNotFoundException(final String message) {
		super(message);
	}

}
