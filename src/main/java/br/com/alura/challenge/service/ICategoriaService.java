package br.com.alura.challenge.service;

import java.util.List;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.InvalidKeyException;

public interface ICategoriaService {

	Categoria salvar(final Categoria categoria) throws InvalidKeyException;

	List<Categoria> buscaTodos();

	Categoria buscaPorId(final Short id);
}
