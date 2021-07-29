package br.com.alura.challenge.repository;

import java.util.List;

import br.com.alura.challenge.domain.entity.Video;

public interface VideoRepositoryCustom {

	List<Video> findByFiltro(final String titulo);
}
