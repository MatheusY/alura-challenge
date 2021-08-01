package br.com.alura.challenge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.exception.VideoNotFoundException;
import br.com.alura.challenge.repository.CategoriaRepository;
import br.com.alura.challenge.repository.VideoRepository;

@Service
public class VideoService implements IVideoService {

	private static final String CATEGORIA_LIVRE = "Livre";

	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	private static Map<String, String> mensagens = new HashMap<>();

	@Override
	public Video salvar(final Video video) throws InvalidKeyException {
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
	public void atualiza(final Video video) throws InvalidKeyException {
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

	private Video gravar(final Video video) throws InvalidKeyException {
		video.setAtivo(true);
		if (Objects.isNull(video.getCategoria())) {
			video.setCategoria(categoriaRepository.findByTituloIgnoreCase(CATEGORIA_LIVRE));
		}
		try {
			return videoRepository.save(video);
		} catch (JpaObjectRetrievalFailureException e) {
			throw new InvalidKeyException(e, mensagens);
		}
	}
}
