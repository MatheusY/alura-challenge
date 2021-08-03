package br.com.alura.challenge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.InvalidKeyException;

public interface ICategoriaService {

	Categoria salvar(final Categoria categoria) throws InvalidKeyException;

	Page<Categoria> buscaTodos(final Pageable pageable);

	Categoria buscaPorId(final Short id);

	void atualiza(final Categoria categoria) throws InvalidKeyException;

	void apagar(final Short id);
}
