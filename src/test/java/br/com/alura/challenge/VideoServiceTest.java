package br.com.alura.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.VideoNotFoundException;
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
		Video videoSalvo = createVideo();
		videoSalvo.setId(1L);
		when(videoRepository.save(any())).thenReturn(videoSalvo);
		Video novoVideo = videoService.salvar(createVideo());
		verify(videoRepository).save(any());
		compareVideo(videoSalvo, novoVideo);
	}

	@Test
	public void testErroSalvar() {
		when(videoRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
		assertThrows(DataIntegrityViolationException.class, () -> videoService.salvar(createVideo()));
		verify(videoRepository).save(any());
	}

	@Test
	public void testBuscaTodos() {
		when(videoRepository.findAll()).thenReturn(List.of(createVideo()));
		List<Video> todos = videoService.buscarTodos();
		verify(videoRepository).findAll();
		assertEquals(1, todos.size());
	}

	@Test
	public void testBuscaTodosSemResultado() {
		when(videoRepository.findAll()).thenReturn(new ArrayList<>());
		List<Video> todos = videoService.buscarTodos();
		verify(videoRepository).findAll();
		assertEquals(0, todos.size());
	}

	@Test
	void testBuscaPorId() {
		Video video = createVideo();
		video.setId(1L);
		when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
		Video videoSalvo = videoService.buscarPorId(video.getId());
		verify(videoRepository).findById(1L);
		compareVideo(video, videoSalvo);
	}

	@Test
	void testBuscaPorIdNaoEncontrado() {
		when(videoRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(VideoNotFoundException.class, () -> videoService.buscarPorId(1L));
		verify(videoRepository).findById(1L);
	}

	@Test
	void testAtualiza() {
		Video video = createVideo();
		video.setId(1L);
		when(videoRepository.existsById(1L)).thenReturn(true);
		when(videoRepository.save(video)).thenReturn(createVideo());
		videoService.atualiza(video);
		verify(videoRepository).existsById(1L);
		verify(videoRepository).save(video);
	}

	@Test
	void testAtualizaNaoEncontrado() {
		Video video = createVideo();
		video.setId(1L);
		when(videoRepository.existsById(1L)).thenReturn(false);
		assertThrows(VideoNotFoundException.class, () -> videoService.atualiza(video));
		verify(videoRepository).existsById(1L);
		verify(videoRepository, times(0)).save(any());
	}

	@Test
	void testApagar() {
		when(videoRepository.existsById(1L)).thenReturn(true);
		videoService.apagar(1L);
		verify(videoRepository).existsById(1L);
		verify(videoRepository).deleteById(1L);
	}

	@Test
	void testApagarNaoEncontrado() {
		when(videoRepository.existsById(1L)).thenReturn(false);
		assertThrows(VideoNotFoundException.class, () -> videoService.apagar(1L));
		verify(videoRepository).existsById(1L);
		verify(videoRepository, times(0)).deleteById(any());
	}

	private Video createVideo() {
		Video video = new Video();
		video.setTitulo("Titulo test");
		video.setDescricao("Descricao Test");
		video.setUrl("Url Test");
		return video;
	}

	private void compareVideo(Video videoEsperado, Video videoAtual) {
		assertEquals(videoEsperado.getId(), videoAtual.getId());
		assertEquals(videoEsperado.getDescricao(), videoAtual.getDescricao());
		assertEquals(videoEsperado.getTitulo(), videoAtual.getTitulo());
		assertEquals(videoEsperado.getUrl(), videoAtual.getUrl());
	}

}
