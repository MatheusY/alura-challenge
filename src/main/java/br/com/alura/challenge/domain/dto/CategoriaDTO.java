package br.com.alura.challenge.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

@Data
public class CategoriaDTO {

	@NotNull(message = "Titulo é obrigatório")
	@Length(max = 30, message = "O tamanho máximo do título é de 30 caracteres")
	private String titulo;

	@NotNull(message = "Cor é obrigatório")
	@Length(max = 6, message = "O tamanho máximo da cor é de 6 caracteres")
	private String cor;
}
