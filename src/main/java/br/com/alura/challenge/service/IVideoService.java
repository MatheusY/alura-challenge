package br.com.alura.challenge.service;

import java.util.List;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.InvalidKeyException;

public interface IVideoService {

	Video salvar(final Video video) throws InvalidKeyException;

	List<Video> buscarFiltro(String search);

	Video buscarPorId(final Long id);

	void atualiza(final Video video) throws InvalidKeyException;

	void apagar(final Long id);

	List<Video> buscaPorCategoria(final Short idCategoria);

}
