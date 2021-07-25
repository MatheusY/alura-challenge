package br.com.alura.challenge;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.repository.VideoRepository;
import br.com.alura.challenge.service.VideoService;

public class VideoServiceTest {

	@InjectMocks
	private VideoService videoService;

	@Mock
	private VideoRepository videoRepository;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testSalvarVideo() {
		videoService.salvar(createVideo());
		verify(videoRepository).save(any());
	}

	@Test
	public void testErroSalvar() {
		when(videoRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
		assertThrows(DataIntegrityViolationException.class, () -> videoService.salvar(createVideo()));
		verify(videoRepository).save(any());
	}

	private Video createVideo() {
		Video video = new Video();
		video.setTitulo("Titulo test");
		video.setDescricao("Descricao Test");
		video.setUrl("Url Test");
		return video;
	}

}
