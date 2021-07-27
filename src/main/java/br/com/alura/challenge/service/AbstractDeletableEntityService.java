package br.com.alura.challenge.service;

import br.com.alura.challenge.domain.entity.DeletableEntity;

public abstract class AbstractDeletableEntityService<E extends DeletableEntity> {

	@SuppressWarnings("null")
	protected void ativar(final E entity) {
		entity.setAtivo(true);
	}

}
