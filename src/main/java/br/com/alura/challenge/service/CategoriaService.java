package br.com.alura.challenge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.CategoriaNotFoundException;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.repository.CategoriaRepository;

@Service
public class CategoriaService extends AbstractDeletableEntityService<Categoria> implements ICategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	private static Map<String, String> mensagens = new HashMap<>();

	static {
		mensagens.put("UK_CATEGORIA_TITULO", "Categoria com o título já cadastrado!");
	}

	@Override
	public Categoria salvar(final Categoria categoria) throws InvalidKeyException {
		return gravar(categoria);
	}

	@Override
	public List<Categoria> buscaTodos() {
		return categoriaRepository.findAll();
	}

	@Override
	public Categoria buscaPorId(final Short id) {
		return categoriaRepository.findById(id).orElseThrow(CategoriaNotFoundException::new);
	}

	@Override
	public void atualiza(final Categoria categoria) throws InvalidKeyException {
		verificaSeExiste(categoria.getId());
		gravar(categoria);
	}

	private Categoria gravar(final Categoria categoria) throws InvalidKeyException {
		ativar(categoria);
		try {
			return categoriaRepository.save(categoria);
		} catch (DataIntegrityViolationException e) {
			throw new InvalidKeyException(e, mensagens);
		}
	}

	private void verificaSeExiste(final Short id) {
		if (!categoriaRepository.existsById(id)) {
			throw new CategoriaNotFoundException();
		}
	}

}
