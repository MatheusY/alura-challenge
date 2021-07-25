package br.com.alura.challenge.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

	@Autowired
	private ModelMapper modelMapper;

	protected <E, D> D convert(final E entity, final Class<D> clazz) {
		return modelMapper.map(entity, clazz);
	}

}
