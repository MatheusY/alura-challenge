package br.com.alura.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.repository.VideoRepository;

@Service
public class VideoService implements IVideoService {

	@Autowired
	private VideoRepository videoRepository;

	@Override
	public Video salvar(Video video) {
		return videoRepository.save(video);
	}

	@Override
	public List<Video> buscarTodos() {
		return videoRepository.findAll();
	}

}
