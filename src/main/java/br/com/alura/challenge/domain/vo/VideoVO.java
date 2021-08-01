package br.com.alura.challenge.domain.vo;

import java.util.Objects;

import lombok.Data;

import br.com.alura.challenge.domain.entity.Categoria;

@Data
public class VideoVO {

	private Long id;

	private String titulo;

	private String descricao;

	private String url;

	private Short categoriaId;

	public void setCategoria(Categoria categoria) {
		categoriaId = Objects.nonNull(categoria) ? categoria.getId() : null;
	}
}
