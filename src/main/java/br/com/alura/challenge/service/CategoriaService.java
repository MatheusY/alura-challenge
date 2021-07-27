package br.com.alura.challenge.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.domain.entity.Categoria;
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
	public Categoria salvar(Categoria categoria) throws InvalidKeyException {
		ativar(categoria);
		try {
			return categoriaRepository.save(categoria);
		} catch (DataIntegrityViolationException e) {
			throw new InvalidKeyException(e, mensagens);
		}
	}

}
