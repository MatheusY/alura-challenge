package br.com.alura.challenge;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.CategoriaNotFoundException;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.repository.CategoriaRepository;
import br.com.alura.challenge.service.CategoriaService;

public class CategoriaServiceTest {

	private static final DataIntegrityViolationException DATA_INTEGRITY_EXCEPTION = new DataIntegrityViolationException("e\"erro\"", new Throwable());

	@InjectMocks
	private CategoriaService categoriaService;

	@Mock
	private CategoriaRepository categoriaRepository;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testSalvarVideo() throws InvalidKeyException {
		Categoria categoria = createCategoria();
		categoria.setId((short) 1);
		categoria.setAtivo(true);
		when(categoriaRepository.save(any())).thenReturn(categoria);
		Categoria categoriaSalva = categoriaService.salvar(createCategoria());
		verify(categoriaRepository).save(any());
		compareCategoria(categoria, categoriaSalva);
	}

	@Test
	public void testSalvarErro() {
		Categoria categoria = createCategoria();
		when(categoriaRepository.save(categoria)).thenThrow(DATA_INTEGRITY_EXCEPTION);
		assertThrows(InvalidKeyException.class, () -> categoriaService.salvar(categoria));
		verify(categoriaRepository).save(categoria);
	}

	@Test
	public void testBuscaTodos() {
		when(categoriaRepository.findAll()).thenReturn(List.of(createCategoria()));
		List<Categoria> categorias = categoriaService.buscaTodos();
		verify(categoriaRepository).findAll();
		assertEquals(1, categorias.size());
	}

	@Test
	public void testBuscaTodosSemResultado() {
		when(categoriaRepository.findAll()).thenReturn(new ArrayList<>());
		List<Categoria> categorias = categoriaService.buscaTodos();
		verify(categoriaRepository).findAll();
		assertTrue(categorias.isEmpty());
	}

	@Test
	public void testBuscaPorId() {
		when(categoriaRepository.findById((short) 1)).thenReturn(Optional.of(createCategoria()));
		categoriaService.buscaPorId((short) 1);
		verify(categoriaRepository).findById((short) 1);
	}

	@Test
	public void testBuscaPorIdNaoEncontrado() {
		when(categoriaRepository.findById((short) 1)).thenReturn(Optional.empty());
		assertThrows(CategoriaNotFoundException.class, () -> categoriaService.buscaPorId((short) 1));
		verify(categoriaRepository).findById((short) 1);
	}

	private Categoria createCategoria() {
		Categoria categoria = new Categoria();
		categoria.setTitulo("Test titulo");
		categoria.setCor("Cor");
		return categoria;
	}

	private void compareCategoria(Categoria expected, Categoria actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getTitulo(), actual.getTitulo());
		assertEquals(expected.getCor(), actual.getCor());
		assertEquals(expected.getAtivo(), actual.getAtivo());
	}

}
