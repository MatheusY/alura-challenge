package br.com.alura.challenge.domain.dto;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import br.com.alura.challenge.domain.entity.Categoria;

@Data
public class VideoDTO {

	@Valid
	@NotNull(message = "Titulo é obrigatório")
	@Length(max = 40, message = "O tamanho máximo do titulo é de 40 caracteres")
	private String titulo;

	@Valid
	@NotNull(message = "Descrição é obrigatório")
	@Length(max = 300, message = "O tamanho máximo da descrição é de 300 caracteres")
	private String descricao;

	@Valid
	@NotNull(message = "URL é obrigatório")
	@Length(max = 100, message = "O tamanho máximo da url é de 100 caracteres")
	private String url;

	private Short categoriaId;

	public Categoria getCategoria() {
		return Objects.nonNull(categoriaId) ? new Categoria(categoriaId) : null;
	}
}
