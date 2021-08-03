package br.com.alura.challenge.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import br.com.alura.challenge.ChallengeApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ChallengeApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = AbstractControllerTest.DockerPostgreDataSourceInitializer.class)
public abstract class AbstractControllerTest {

	public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:11");

	static {
		postgreDBContainer.start();
	}

	public static class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {

			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreDBContainer.getUsername(), "spring.datasource.password=" + postgreDBContainer.getPassword());
		}
	}

	public static final Faker faker = new Faker(new Locale("pt", "BR"));

	static final String RESOURCE_BASE_URL = "/";

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	protected ModelMapper modelMapper;

	public <D, T> List<D> convert(final List<T> models, final Class<D> clazz) {
		List<D> dtos = new ArrayList<>();
		for (T model : models) {
			dtos.add(modelMapper.map(model, clazz));
		}

		return dtos;
	}

	public <D, T> D convert(final T model, final Class<D> clazz) {
		return modelMapper.map(model, clazz);
	}

	protected HttpEntity<?> getRequestEntity() {
		return new HttpEntity<>(getHeaders());
	}

	protected HttpEntity<Object> getRequestEntity(Object object) {
		return new HttpEntity<>(new Gson().toJson(object), getHeaders());
	}

	protected HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	protected void assertResponseGet(ResponseEntity<?> responseGet) {
		assertResponse(responseGet, HttpStatus.OK);
		
	}

	protected void assertResponse(ResponseEntity<?> responseGet, HttpStatus httpStatus) {
		assertNotNull(responseGet);
		if (HttpStatus.NO_CONTENT.equals(httpStatus)) {
			assertNull(responseGet.getBody());
		} else {
			assertNotNull(responseGet.getBody());
		}
		assertThat(responseGet.getStatusCode(), equalTo(httpStatus));
	}

}
