package br.com.alura.challenge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.InvalidKeyException;

public interface IVideoService {

	Video salvar(final Video video) throws InvalidKeyException;

	Page<Video> buscarFiltro(final String search, final Pageable pageable);

	Video buscarPorId(final Long id);

	void atualiza(final Video video) throws InvalidKeyException;

	void apagar(final Long id);

	Page<Video> buscaPorCategoria(final Short idCategoria, final Pageable pageable);

}
