package br.com.alura.challenge.exception;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidKeyException extends BusinessException {

	private static final Pattern KEY_NAME = Pattern.compile("(?<=\")(.*)(?=\")");
	private static final long serialVersionUID = 7629952010299161902L;

	public InvalidKeyException(DataIntegrityViolationException e, Map<String, String> message) {
		super(getMessage(e, message), HttpStatus.BAD_REQUEST);
	}

	private static String getMessage(DataIntegrityViolationException e, Map<String, String> message) {
		String keyName = e.getRootCause().toString().toUpperCase();
		Matcher m = KEY_NAME.matcher(keyName);

		String match = "";
		while (m.find()) {
			match = m.group();
		}

		return message.get(match);
	}

}
