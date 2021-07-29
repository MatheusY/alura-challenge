package br.com.alura.challenge.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.VideoNotFoundException;
import br.com.alura.challenge.repository.VideoRepository;

@Service
public class VideoService implements IVideoService {

	@Autowired
	private VideoRepository videoRepository;

	@Override
	public Video salvar(final Video video) {
		return gravar(video);
	}

	@Override
	public List<Video> buscarFiltro(final String search) {
		return videoRepository.findByFiltro(search);
	}

	@Override
	public Video buscarPorId(final Long id) {
		return videoRepository.findById(id).orElseThrow(VideoNotFoundException::new);
	}

	@Override
	public void atualiza(final Video video) {
		verificaSeExiste(video.getId());
		gravar(video);
	}

	@Override
	public void apagar(Long id) {
		verificaSeExiste(id);
		videoRepository.deleteById(id);
	}

	@Override
	public List<Video> buscaPorCategoria(final Short idCategoria) {
		return videoRepository.findByCategoriaId(idCategoria);
	}

	private void verificaSeExiste(final Long id) {
		if (!videoRepository.existsById(id)) {
			throw new VideoNotFoundException();
		}
	}

	private Video gravar(final Video video) {
		video.setAtivo(true);
		if (Objects.isNull(video.getCategoria())) {
			video.setCategoria(new Categoria((short) 1));
		}
		return videoRepository.save(video);
	}
}
