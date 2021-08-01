package br.com.alura.challenge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.alura.challenge.domain.dto.CategoriaDTO;
import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.entity.FieldError;
import br.com.alura.challenge.domain.vo.CategoriaVO;
import br.com.alura.challenge.domain.vo.ErrorResponse;
import br.com.alura.challenge.repository.CategoriaRepository;

public class CategoriaControllerTest extends AbstractControllerTest {

	@ClassRule
	//	public static PostgreSQLContainer postgreSQLContainer = BaeldungPostgresqlContainer.getInstance();

	private static final String CATEGORIA_BASE_URL = RESOURCE_BASE_URL + "categorias/";

	private static final String MSG_TITULO_CADASTRADO = "Categoria com o título já cadastrado!";

	//	static {
	//		postgreSQLContainer.start();
	//	}

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Nested
	@DisplayName("GET")
	class Get {

		@Nested
		@DisplayName("Busca com sucesso")
		class GetSucess {

			@Test
			@DisplayName("Busca todos trazendo uma categoria")
			public void testGet() {
				List<Categoria> categorias = List.of(createCategoria());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);
				ParameterizedTypeReference<List<CategoriaVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<List<CategoriaVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertCategorias(convert(categorias, CategoriaVO.class), responseGet.getBody());
			}

			@Test
			@DisplayName("Busca todos trazendo mais de uma categoria")
			public void testGetNoResults() {
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);
				ParameterizedTypeReference<List<CategoriaVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<List<CategoriaVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertThat(responseGet.getBody(), hasSize(0));
			}

			@Test
			@DisplayName("Busca todos trazendo mais de uma categoria")
			public void testGetMoreCategorias() {
				List<Categoria> categorias = List.of(createCategoria(), createCategoria());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);
				ParameterizedTypeReference<List<CategoriaVO>> responseType = new ParameterizedTypeReference<>() {};
				ResponseEntity<List<CategoriaVO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertResponseGet(responseGet);
				assertCategorias(convert(categorias, CategoriaVO.class), responseGet.getBody());
			}

			@Test
			@DisplayName("Busca por id")
			public void testGetById() {
				Categoria categoria = createCategoria();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());
				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);

				assertResponseGet(responseGet);
				assertCategoria(convert(categoria, CategoriaVO.class), responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Busca com erro")
		class GetError {

			@Test
			@DisplayName("Busca por id não encontrado")
			public void testGetByIdNotFound() {
				createCategoria();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);
				url.append("/9999");

				ResponseEntity<ErrorResponse> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);
				assertResponseNotFound(responseGet);
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
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);

				ResponseEntity<Short> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(categoriaDTO), Short.class);

				assertResponse(responsePost, HttpStatus.CREATED);
				CategoriaVO categoriaVO = convert(categoriaDTO, CategoriaVO.class);
				categoriaVO.setId(responsePost.getBody());

				url.append("/").append(categoriaVO.getId());
				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);

				assertResponseGet(responseGet);
				assertCategoria(categoriaVO, responseGet.getBody());
			}

			@Test
			@DisplayName("Cadastro com uma categoria que tem essa cor")
			public void testPostWithTheSameColor() {
				Categoria categoriaCreated = createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				categoriaDTO.setCor(categoriaCreated.getCor());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);

				ResponseEntity<Short> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(categoriaDTO), Short.class);

				assertResponse(responsePost, HttpStatus.CREATED);
				CategoriaVO categoriaVO = convert(categoriaDTO, CategoriaVO.class);
				categoriaVO.setId(responsePost.getBody());

				url.append("/").append(categoriaVO.getId());
				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);

				assertResponseGet(responseGet);
				assertCategoria(categoriaVO, responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Cadastro com falha")
		class PostFailure {

			@Test
			@DisplayName("Quando os campos são nulos")
			public void testPostNullFields() {
				CategoriaDTO categoria = new CategoriaDTO();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);

				ResponseEntity<ErrorResponse> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(categoria), ErrorResponse.class);

				assertRequiredFields(responsePost);
			}

			@Test
			@DisplayName("Quando os campos são inválidos")
			public void testPostInvalidFields() {
				CategoriaVO categoria = new CategoriaVO();
				categoria.setCor(StringUtils.repeat('a', 7));
				categoria.setTitulo(StringUtils.repeat('a', 31));
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);

				ResponseEntity<ErrorResponse> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(categoria), ErrorResponse.class);

				assertInvalidFields(responsePost);
			}

			@Test
			@DisplayName("Quando já uma categoria com o mesmo titulo")
			public void testPostWithTheSameTitulo() {
				Categoria categoriaCreated = createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				categoriaDTO.setTitulo(categoriaCreated.getTitulo());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL);

				ResponseEntity<ErrorResponse> responsePost = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(categoriaDTO), ErrorResponse.class);

				assertResponse(responsePost, HttpStatus.BAD_REQUEST);
				assertThat(responsePost.getBody().getMessage(), equalTo(MSG_TITULO_CADASTRADO));
			}
		}
	}

	@Nested
	@DisplayName("PUT")
	class Put {

		@Nested
		@DisplayName("Atualização com sucesso")
		class PutSuccess {

			@Test
			@DisplayName("Atualização com sucesso")
			public void testPut() {
				Categoria categoria = createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<Void> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaDTO), Void.class);

				assertResponse(responsePut, HttpStatus.NO_CONTENT);
				CategoriaVO categoriaVO = convert(categoriaDTO, CategoriaVO.class);
				categoriaVO.setId(categoria.getId());

				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);

				assertResponseGet(responseGet);
				assertCategoria(categoriaVO, responseGet.getBody());
			}

			@Test
			@DisplayName("Atualização com uma categoria que tem essa cor")
			public void testPutWithTheSameColor() {
				Categoria categoriaCreated = createCategoria();
				Categoria categoria = createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				categoriaDTO.setCor(categoriaCreated.getCor());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<Void> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaDTO), Void.class);

				assertResponse(responsePut, HttpStatus.NO_CONTENT);
				CategoriaVO categoriaVO = convert(categoriaDTO, CategoriaVO.class);
				categoriaVO.setId(categoria.getId());

				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);

				assertResponseGet(responseGet);
				assertCategoria(categoriaVO, responseGet.getBody());
			}
		}

		@Nested
		@DisplayName("Atualização com falha")
		class PutFailure {

			@Test
			@DisplayName("Categoria não existente")
			public void testPutNotFound() {
				createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/9999");

				ResponseEntity<ErrorResponse> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaDTO), ErrorResponse.class);

				assertResponseNotFound(responsePut);
			}

			@Test
			@DisplayName("Quando os campos são nulos")
			public void testPutNullFields() {
				Categoria categoria = createCategoria();
				CategoriaVO categoriaVazia = new CategoriaVO();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<ErrorResponse> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaVazia), ErrorResponse.class);

				assertRequiredFields(responsePut);
			}

			@Test
			@DisplayName("Quando os campos são inválidos")
			public void testPutInvalidFields() {
				Categoria categoria = createCategoria();
				CategoriaDTO categoriaDTO = new CategoriaDTO();
				categoriaDTO.setCor(StringUtils.repeat('a', 7));
				categoriaDTO.setTitulo(StringUtils.repeat('a', 31));
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<ErrorResponse> responsePost = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaDTO), ErrorResponse.class);

				assertInvalidFields(responsePost);
			}

			@Test
			@DisplayName("Quando já uma categoria com o mesmo titulo")
			public void testPutWithTheSameTitulo() {
				Categoria categoriaCreated = createCategoria();
				Categoria categoria = createCategoria();
				CategoriaDTO categoriaDTO = convert(buildCategoria(), CategoriaDTO.class);
				categoriaDTO.setTitulo(categoriaCreated.getTitulo());
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<ErrorResponse> responsePut = restTemplate.exchange(url.toString(), HttpMethod.PUT, getRequestEntity(categoriaDTO), ErrorResponse.class);

				assertResponse(responsePut, HttpStatus.BAD_REQUEST);
				assertThat(responsePut.getBody().getMessage(), equalTo(MSG_TITULO_CADASTRADO));
			}
		}
	}

	@Nested
	@DisplayName("DELETE")
	class Delete {

		@Nested
		@DisplayName("Remoção com sucesso")
		class DeleteSuccess {

			@Test
			@DisplayName("Remoção com sucesso")
			public void testDelete() {
				Categoria categoria = createCategoria();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());

				ResponseEntity<Void> responseDelete = restTemplate.exchange(url.toString(), HttpMethod.DELETE, getRequestEntity(), Void.class);

				assertResponse(responseDelete, HttpStatus.NO_CONTENT);

				ResponseEntity<ErrorResponse> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);
				assertResponseNotFound(responseGet);
			}
		}

		@Nested
		@DisplayName("Remoção com falha")
		class DeleteFailure {

			@Test
			@DisplayName("Remoção com sucesso")
			public void testDeleteNotFound() {
				Categoria categoria = createCategoria();
				StringBuilder url = new StringBuilder(CATEGORIA_BASE_URL).append("/9999");

				ResponseEntity<ErrorResponse> responseDelete = restTemplate.exchange(url.toString(), HttpMethod.DELETE, getRequestEntity(), ErrorResponse.class);

				assertResponseNotFound(responseDelete);

				url = new StringBuilder(CATEGORIA_BASE_URL).append("/").append(categoria.getId());
				ResponseEntity<CategoriaVO> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), CategoriaVO.class);
				assertResponseGet(responseGet);
			}
		}

	}

	@AfterEach
	private void afterEach() {
		categoriaRepository.deleteAllInBatch();
	}

	private void assertResponseNotFound(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.NOT_FOUND);
		assertThat(response.getBody().getMessage(), equalTo("Categoria não encontrada!"));
	}

	private void assertCategorias(List<CategoriaVO> expected, List<CategoriaVO> actual) {
		assertEquals(expected.size(), actual.size());
		for (CategoriaVO actualCategoria : actual) {
			assertCategoria(expected.stream().filter(expectedCategoria -> expectedCategoria.getId().equals(actualCategoria.getId())).findFirst().orElse(new CategoriaVO()), actualCategoria);
		}

	}

	private void assertCategoria(CategoriaVO expected, CategoriaVO actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getCor(), actual.getCor());
		assertEquals(expected.getTitulo(), actual.getTitulo());
	}

	private void assertRequiredFields(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.BAD_REQUEST);

		assertThat(response.getBody().getFieldErros(), hasSize(2));
		FieldError expectedTitulo = new FieldError("titulo", "Titulo é obrigatório");
		FieldError expectedCor = new FieldError("cor", "Cor é obrigatório");

		assertThat(response.getBody().getFieldErros(), hasItems(expectedTitulo));
		assertThat(response.getBody().getFieldErros(), hasItems(expectedCor));
	}

	private void assertInvalidFields(ResponseEntity<ErrorResponse> response) {
		assertResponse(response, HttpStatus.BAD_REQUEST);

		assertThat(response.getBody().getFieldErros(), hasSize(2));
		FieldError expectedTitulo = new FieldError("titulo", "O tamanho máximo do título é de 30 caracteres");
		FieldError expectedCor = new FieldError("cor", "O tamanho máximo da cor é de 6 caracteres");

		assertThat(response.getBody().getFieldErros(), hasItems(expectedTitulo));
		assertThat(response.getBody().getFieldErros(), hasItems(expectedCor));

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
