package br.com.alura.challenge.service;

import java.util.List;

import br.com.alura.challenge.domain.entity.Video;

public interface IVideoService {

	Video salvar(final Video video);

	List<Video> buscarFiltro(String search);

	Video buscarPorId(final Long id);

	void atualiza(final Video video);

	void apagar(final Long id);

	List<Video> buscaPorCategoria(final Short idCategoria);

}
