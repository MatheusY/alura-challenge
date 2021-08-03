package br.com.alura.challenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alura.challenge.domain.entity.Video;

public interface VideoRepositoryCustom {

	Page<Video> findByFiltro(final String titulo, final Pageable pageable);
}
