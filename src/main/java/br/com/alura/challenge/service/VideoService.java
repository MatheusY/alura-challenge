package br.com.alura.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.VideoNotFoundException;
import br.com.alura.challenge.repository.VideoRepository;

@Service
public class VideoService implements IVideoService {

	@Autowired
	private VideoRepository videoRepository;

	@Override
	public Video salvar(final Video video) {
		video.setAtivo(true);
		return videoRepository.save(video);
	}

	@Override
	public List<Video> buscarTodos() {
		return videoRepository.findAll();
	}

	@Override
	public Video buscarPorId(final Long id) {
		return videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
	}

	@Override
	public void atualiza(final Video video) {
		verificaSeExiste(video.getId());
		video.setAtivo(true);
		videoRepository.save(video);
	}

	@Override
	public void apagar(Long id) {
		verificaSeExiste(id);
		videoRepository.deleteById(id);
	}

	private void verificaSeExiste(final Long id) {
		if (!videoRepository.existsById(id)) {
			throw new VideoNotFoundException();
		}
	}
}
