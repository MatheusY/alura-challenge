package br.com.alura.challenge.service;

import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.InvalidKeyException;

public interface ICategoriaService {

	Categoria salvar(final Categoria categoria) throws InvalidKeyException;
}
