package br.com.alura.challenge.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.exception.VideoNotFoundException;
import br.com.alura.challenge.repository.CategoriaRepository;
import br.com.alura.challenge.repository.VideoRepository;

public class VideoServiceTest {

	private static final JpaObjectRetrievalFailureException JPA_EXCEPTION = new JpaObjectRetrievalFailureException(new EntityNotFoundException("e\"erro\""));

	@InjectMocks
	private VideoService videoService;

	@Mock
	private VideoRepository videoRepository;
	
	@Mock
	private CategoriaRepository categoriaRepository;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testSalvarVideo() throws InvalidKeyException {
		Video videoSalvo = createVideo();
		videoSalvo.setId(1L);
		when(videoRepository.save(any())).thenReturn(videoSalvo);
		Video novoVideo = videoService.salvar(createVideo());
		verify(videoRepository).save(any());
		compareVideo(videoSalvo, novoVideo);
	}

	@Test
	public void testSalvarVideoSemCategoria() throws InvalidKeyException {
		Video videoSalvo = createVideo();
		videoSalvo.setId(1L);
		when(videoRepository.save(any())).thenReturn(videoSalvo);
		when(categoriaRepository.findByTituloIgnoreCase(any())).thenReturn(new Categoria((short) 1));
		Video videoNaoSalvo = createVideo();
		videoNaoSalvo.setCategoria(null);
		Video novoVideo = videoService.salvar(videoNaoSalvo);
		verify(videoRepository).save(any());
		compareVideo(videoSalvo, novoVideo);
	}

	@Test
	public void testErroSalvar() {
		when(videoRepository.save(any())).thenThrow(JPA_EXCEPTION);
		assertThrows(InvalidKeyException.class, () -> videoService.salvar(createVideo()));
		verify(videoRepository).save(any());
	}

	@Test
	public void testBuscaTodos() {
		Pageable pageable = PageRequest.of(0, 1);
		when(videoRepository.findByFiltro(null, pageable)).thenReturn(new PageImpl<>(List.of(createVideo()), pageable, 1));
		Page<Video> todos = videoService.buscarFiltro(null, pageable);
		verify(videoRepository).findByFiltro(null, pageable);
		assertEquals(1, todos.getContent().size());
	}

	@Test
	public void testBuscaTodosSemResultado() {
		Pageable pageable = PageRequest.of(0, 1);
		when(videoRepository.findByFiltro("a", pageable)).thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));
		Page<Video> todos = videoService.buscarFiltro("a", pageable);
		verify(videoRepository).findByFiltro("a", pageable);
		assertTrue(todos.isEmpty());
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
	void testAtualiza() throws InvalidKeyException {
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

	@Test
	void testBuscaPorCategoria() {
		Pageable pageable = PageRequest.of(0, 1);
		Short id = 1;
		when(videoRepository.findByCategoriaId(id, pageable)).thenReturn(new PageImpl<>(List.of(createVideo()),pageable, 1));
		Page<Video> videos = videoService.buscaPorCategoria(id, pageable);
		verify(videoRepository).findByCategoriaId(id, pageable);
		assertEquals(1, videos.getContent().size());
	}

	@Test
	void testBuscaPorCategoriaSemVideo() {
		Pageable pageable = PageRequest.of(0, 1);
		Short id = 1;
		when(videoRepository.findByCategoriaId(id, pageable)).thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));
		Page<Video> videos = videoService.buscaPorCategoria(id, pageable);
		verify(videoRepository).findByCategoriaId(id, pageable);
		assertTrue(videos.isEmpty());
	}

	private Video createVideo() {
		Video video = new Video();
		video.setTitulo("Titulo test");
		video.setDescricao("Descricao Test");
		video.setUrl("Url Test");
		video.setCategoria(new Categoria((short) 1));
		video.setAtivo(true);
		return video;
	}

	private void compareVideo(Video videoEsperado, Video videoAtual) {
		assertEquals(videoEsperado.getId(), videoAtual.getId());
		assertEquals(videoEsperado.getDescricao(), videoAtual.getDescricao());
		assertEquals(videoEsperado.getTitulo(), videoAtual.getTitulo());
		assertEquals(videoEsperado.getUrl(), videoAtual.getUrl());
		assertEquals(videoEsperado.getCategoria().getId(), videoAtual.getCategoria().getId());
	}

}
