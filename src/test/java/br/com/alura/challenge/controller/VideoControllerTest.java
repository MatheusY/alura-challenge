package br.com.alura.challenge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.alura.challenge.domain.dto.VideoDTO;
import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.entity.FieldError;
import br.com.alura.challenge.domain.entity.RestResponsePage;
import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.domain.vo.ErrorResponse;
import br.com.alura.challenge.domain.vo.VideoVO;
import br.com.alura.challenge.repository.CategoriaRepository;
import br.com.alura.challenge.repository.VideoRepository;

public class VideoControllerTest extends AbstractControllerTest {

	private static final String VIDEO_BASE_URL = RESOURCE_BASE_URL + "video/";

	@Autowired
	private VideoRepository videoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	private Categoria categoriaLivre;

	@Nested
	@DisplayName("GET")
	class Get {

		@Nested
		@DisplayName("Busca com sucesso")
		class GetSucess {

			@Test
			@DisplayName("Busca sem parametro trazendo um video")
			public void testGet() {
				Pageable pageable = PageRequest.of(0, 3);
				List<Video> videos = createVideos();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append(String.format("?page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, videos.size());
				assertVideos(convert(videos, VideoVO.class), responseGet.getBody().getContent());
			}

			@Test
			@DisplayName("Busca sem parametro sem trazer nenhum resultado")
			public void testGetNoResults() {
				Pageable pageable = PageRequest.of(0, 3);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append(String.format("?page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, 0);
				assertThat(responseGet.getBody().getContent(), hasSize(0));
			}

			@Test
			@DisplayName("Busca sem parametro trazendo mais de uma pagina")
			public void testGetMoreVideosPages() {
				Pageable pageable = PageRequest.of(0, 2);
				List<Video> videos = createVideos();
				videos.add(createVideo());
				videos.add(createVideo());
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append(String.format("?page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, videos.size());
				videos.remove(2);
				assertVideos(convert(videos, VideoVO.class), responseGet.getBody().getContent());
			}

			@Test
			@DisplayName("Busca com parametro do titulo trazendo video")
			public void testGetByFiltro() {
				Pageable pageable = PageRequest.of(0, 3);
				List<Video> videos = createVideos();
				createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append("?search=").append(videos.get(0).getTitulo());
				url.append(String.format("&page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, videos.size());
				assertVideos(convert(videos, VideoVO.class), responseGet.getBody().getContent());
			}

			@Test
			@DisplayName("Busca com parametro apenas com o come??o do titulo trazendo video")
			public void testGetByFiltroStartsWith() {
				Pageable pageable = PageRequest.of(0, 3);
				List<Video> videos = createVideos();
				createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append("?search=").append(videos.get(0).getTitulo().substring(0, 10));
				url.append(String.format("&page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, videos.size());
				assertVideos(convert(videos, VideoVO.class), responseGet.getBody().getContent());
			}

			@Test
			@DisplayName("Busca com parametro apenas com o final do titulo trazendo video")
			public void testGetByFiltroEndWith() {
				Pageable pageable = PageRequest.of(0, 3);
				List<Video> videos = createVideos();
				createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append("?search=").append(videos.get(0).getTitulo().substring(10));
				url.append(String.format("&page=%d", pageable.getPageNumber()));
				url.append(String.format("&size=%d", pageable.getPageSize()));
				ParameterizedTypeReference<RestResponsePage<VideoVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<RestResponsePage<VideoVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertPage(responseGet, pageable, videos.size());
				assertVideos(convert(videos, VideoVO.class), responseGet.getBody().getContent());
			}

			@Test
			@DisplayName("Busca por id")
			public void testGetById() {
				Video video = createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append("/").append(video.getId());
				ResponseEntity<VideoVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), VideoVO.class);

				assertResponseGet(responseGet);
				assertVideo(convert(video, VideoVO.class), responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Busca com erro")
		class GetError {

			@Test
			@DisplayName("Busca por id n??o encontrado")
			public void testGetByIdNotFound() {
				createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				url.append("/999");

				ResponseEntity<ErrorResponse> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);
				assertResponseError(responseGet, HttpStatus.NOT_FOUND);
			}
		}
	}

	@Nested
	@DisplayName("POST")
	class Post {

		@Nested
		@DisplayName("Cadastro com sucesso")
		class PostSuccess {

			@Test
			@DisplayName("Cadastro com sucesso")
			public void testPost() {
				Video video = buildVideo();
				VideoDTO videoDTO = convert(video, VideoDTO.class);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);
				ResponseEntity<Long> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(videoDTO), Long.class);
				assertResponse(responsePost, HttpStatus.CREATED);

				VideoVO videoVO = convert(video, VideoVO.class);
				videoVO.setId(responsePost.getBody());

				url.append("/").append(videoVO.getId());
				ResponseEntity<VideoVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), VideoVO.class);

				assertVideo(videoVO, responseGet.getBody());

			}

			@Test
			@DisplayName("Cadastro com sucesso sem categoria")
			public void testPostWithoutCategoria() {
				Video video = buildVideo();
				video.setCategoria(null);
				VideoDTO videoDTO = convert(video, VideoDTO.class);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);

				ResponseEntity<Long> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(videoDTO), Long.class);
				assertResponse(responsePost, HttpStatus.CREATED);

				VideoVO videoVO = convert(video, VideoVO.class);
				videoVO.setId(responsePost.getBody());
				videoVO.setCategoriaId(categoriaLivre.getId());

				url.append("/").append(videoVO.getId());
				ResponseEntity<VideoVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), VideoVO.class);

				assertVideo(videoVO, responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Cadastro com falha")
		class PostFailure {

			@Test
			@DisplayName("Quando os campos obrigatorios n??o s??o preenchidos")
			public void testPostNullRequiredFields() {
				VideoDTO video = new VideoDTO();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(video), ErrorResponse.class);
				assertRequiredFields(response);
			}

			@Test
			@DisplayName("Quando os campos est??o inv??lidos")
			public void testPostInvalidFields() {
				VideoDTO video = new VideoDTO();
				video.setDescricao(StringUtils.repeat('a', 301));
				video.setTitulo(StringUtils.repeat('a', 41));
				video.setUrl(StringUtils.repeat('a', 101));
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(video), ErrorResponse.class);
				assertInvalidFields(response);
			}

			@Test
			@DisplayName("Quando a categoria n??o existe")
			public void testPostNotFoundCategoria() {
				VideoDTO video = convert(buildVideo(), VideoDTO.class);
				video.setCategoriaId(Short.MAX_VALUE);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL);

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(video), ErrorResponse.class);
				assertResponse(response, HttpStatus.BAD_REQUEST);
			}
		}
	}

	@Nested
	@DisplayName("PUT")
	class Put {

		@Nested
		@DisplayName("Atualiza????o com sucesso")
		class PutSuccess {

			@Test
			@DisplayName("Atualiza????o com sucesso")
			public void testPut() {
				Video video = createVideo();
				Video videoAtualizado = buildVideo();
				VideoDTO videoDTO = convert(videoAtualizado, VideoDTO.class);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());
				ResponseEntity<Void> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoDTO), Void.class);
				assertResponse(responsePut, HttpStatus.NO_CONTENT);

				VideoVO videoVO = convert(videoAtualizado, VideoVO.class);
				videoVO.setId(video.getId());

				ResponseEntity<VideoVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), VideoVO.class);

				assertVideo(videoVO, responseGet.getBody());

			}

			@Test
			@DisplayName("Cadastro com sucesso sem categoria")
			public void testPutWithoutCategoria() {
				Video video = createVideo();
				Video videoAtualizado = buildVideo();
				videoAtualizado.setCategoria(null);
				VideoDTO videoDTO = convert(videoAtualizado, VideoDTO.class);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());

				ResponseEntity<Void> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoDTO), Void.class);
				assertResponse(responsePut, HttpStatus.NO_CONTENT);

				VideoVO videoVO = convert(videoAtualizado, VideoVO.class);
				videoVO.setId(video.getId());
				videoVO.setCategoriaId(categoriaLivre.getId());

				ResponseEntity<VideoVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), VideoVO.class);

				assertVideo(videoVO, responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Atualiza????o com falha")
		class PostFailure {

			@Test
			@DisplayName("Quando n??o existe o video")
			public void testPutNotFound() {
				createVideo();
				Video videoAtualizado = buildVideo();
				VideoDTO videoDTO = convert(videoAtualizado, VideoDTO.class);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/9999");
				ResponseEntity<ErrorResponse> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoDTO), ErrorResponse.class);
				assertResponseNotFound(responsePut);
			}

			@Test
			@DisplayName("Quando os campos obrigatorios n??o s??o preenchidos")
			public void testPutNullRequiredFields() {
				Video video = createVideo();
				VideoDTO videoAtualizado = new VideoDTO();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoAtualizado), ErrorResponse.class);
				assertRequiredFields(response);
			}

			@Test
			@DisplayName("Quando os campos est??o inv??lidos")
			public void testPutInvalidFields() {
				Video video = createVideo();
				VideoDTO videoAtualizado = new VideoDTO();
				videoAtualizado.setDescricao(StringUtils.repeat('a', 301));
				videoAtualizado.setTitulo(StringUtils.repeat('a', 41));
				videoAtualizado.setUrl(StringUtils.repeat('a', 101));
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoAtualizado), ErrorResponse.class);
				assertInvalidFields(response);
			}

			@Test
			@DisplayName("Quando a categoria n??o existe")
			public void testPutNotFoundCategoria() {
				Video video = createVideo();
				VideoDTO videoAtualizado = convert(buildVideo(), VideoDTO.class);
				videoAtualizado.setCategoriaId(Short.MAX_VALUE);
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(videoAtualizado), ErrorResponse.class);
				assertResponse(response, HttpStatus.BAD_REQUEST);
			}
		}
	}

	@Nested
	@DisplayName("DELETE")
	class Delete {

		@Nested
		@DisplayName("Remo????o com sucesso")
		class DeleteSuccess {

			@Test
			@DisplayName("Remo????o com sucesso")
			public void testDelete() {
				Video video = createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/").append(video.getId());

				ResponseEntity<Void> responseDelete = restTemplate.exchange(url.toString(), HttpMethod.DELETE, getRequestEntity(), Void.class);
				assertResponse(responseDelete, HttpStatus.NO_CONTENT);

				ResponseEntity<ErrorResponse> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);
				assertResponseNotFound(responseGet);
			}
		}

		@Nested
		@DisplayName("Remo????o com erro")
		class DeleteFailure {

			@Test
			@DisplayName("Remo????o n??o encontrado")
			public void testDeleteNotFound() {
				createVideo();
				StringBuilder url = new StringBuilder(VIDEO_BASE_URL).append("/999");

				ResponseEntity<ErrorResponse> responseDelete = restTemplate.exchange(url.toString(), HttpMethod.DELETE, getRequestEntity(), ErrorResponse.class);
				assertResponseNotFound(responseDelete);
			}
		}
	}

	@BeforeEach
	void beforeEach() {
		categoriaLivre = buildCategoria();
		categoriaLivre.setTitulo("Livre");
		categoriaRepository.save(categoriaLivre);
	}

	@AfterEach
	void afterEach() {
		videoRepository.deleteAllInBatch();
		categoriaRepository.deleteAllInBatch();
	}

	private void assertVideos(List<@NonNull VideoVO> expected, List<VideoVO> actual) {
		assertEquals(expected.size(), actual.size());
		for (VideoVO actualVideo : actual) {
			assertVideo(expected.stream().filter(expectedVideo -> expectedVideo.getId().equals(actualVideo.getId())).findFirst().orElse(new VideoVO()), actualVideo);
		}

	}

	private void assertVideo(VideoVO expected, VideoVO actual) {
		assertThat(actual.getCategoriaId(), equalTo(expected.getCategoriaId()));
		assertThat(actual.getDescricao(), equalTo(expected.getDescricao()));
		assertThat(actual.getId(), equalTo(expected.getId()));
		assertThat(actual.getTitulo(), equalTo(expected.getTitulo()));
		assertThat(actual.getUrl(), equalTo(expected.getUrl()));
	}
	
	private void assertPage(ResponseEntity<RestResponsePage<VideoVO>> responseGet, Pageable pageable, long totalElements) {
		assertEquals(responseGet.getBody().getSize(), pageable.getPageSize());
		assertEquals(responseGet.getBody().getNumber(), pageable.getPageNumber());
		assertThat(responseGet.getBody().getTotalElements(), equalTo(totalElements));
	}

	private void assertResponseError(ResponseEntity<ErrorResponse> response, HttpStatus httpStatus) {
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertThat(response.getStatusCode(), equalTo(httpStatus));
	}

	private void assertRequiredFields(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.BAD_REQUEST);

		assertThat(response.getBody().getFieldErros(), hasSize(3));
		FieldError expectedTitulo = new FieldError("titulo", "Titulo ?? obrigat??rio");
		FieldError expectedDescricao = new FieldError("descricao", "Descri????o ?? obrigat??rio");
		FieldError expectedUrl = new FieldError("url", "URL ?? obrigat??rio");

		assertThat(response.getBody().getFieldErros(), hasItems(expectedTitulo));
		assertThat(response.getBody().getFieldErros(), hasItems(expectedDescricao));
		assertThat(response.getBody().getFieldErros(), hasItems(expectedUrl));
	}

	private void assertInvalidFields(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.BAD_REQUEST);

		assertThat(response.getBody().getFieldErros(), hasSize(3));
		FieldError invalidTitulo = new FieldError("titulo", "O tamanho m??ximo do titulo ?? de 40 caracteres");
		FieldError invalidDescricao = new FieldError("descricao", "O tamanho m??ximo da descri????o ?? de 300 caracteres");
		FieldError invalidUrl = new FieldError("url", "O tamanho m??ximo da url ?? de 100 caracteres");

		assertThat(response.getBody().getFieldErros(), hasItems(invalidTitulo));
		assertThat(response.getBody().getFieldErros(), hasItems(invalidDescricao));
		assertThat(response.getBody().getFieldErros(), hasItems(invalidUrl));
	}

	private void assertResponseNotFound(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.NOT_FOUND);
		assertThat(response.getBody().getMessage(), equalTo("Video n??o encontrado!"));
	}

	private List<Video> createVideos() {
		List<Video> videos = new ArrayList<>();
		videos.add(createVideo());
		return videos;
	}

	private Video createVideo() {
		Video video = buildVideo();
		videoRepository.save(video);
		assertNotNull(video.getId());
		return video;
	}

	private Video buildVideo() {
		Video video = new Video();
		video.setAtivo(true);
		video.setCategoria(createCategoria());
		video.setDescricao(faker.lorem().characters(100));
		video.setTitulo(faker.lorem().characters(20));
		video.setUrl(faker.lorem().characters(20));
		return video;
	}

	private Categoria createCategoria() {
		Categoria categoria = buildCategoria();
		categoriaRepository.save(categoria);
		assertNotNull(categoria.getId());
		return categoria;
	}

	private Categoria buildCategoria() {
		Categoria categoria = new Categoria();
		categoria.setAtivo(true);
		categoria.setCor(faker.lorem().characters(6));
		categoria.setTitulo(faker.lorem().characters(20));
		return categoria;
	}

}
