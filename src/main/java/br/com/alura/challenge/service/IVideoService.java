package br.com.alura.challenge.service;

import java.util.List;

import br.com.alura.challenge.domain.entity.Video;

public interface IVideoService {

	Video salvar(Video video);

	List<Video> buscarTodos();

}
