package br.com.alura.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class VideoNotFoundException extends RecordNotFoundException {

	private static final String VIDEO_NAO_ENCONTRADO = "Video não encontrado!";
	private static final long serialVersionUID = 7530052784103036707L;

	public VideoNotFoundException() {
		super(VIDEO_NAO_ENCONTRADO);
	}

}
