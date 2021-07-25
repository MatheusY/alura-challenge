package br.com.alura.challenge.domain.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import br.com.alura.challenge.domain.entity.FieldError;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

	private String details;

	private List<FieldError> fieldErros;

}
