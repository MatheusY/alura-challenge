package br.com.alura.challenge.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

	@Autowired
	private ModelMapper modelMapper;

	protected <E, D> D convert(final E entity, final Class<D> clazz) {
		return modelMapper.map(entity, clazz);
	}

	protected <E, D> List<D> convert(final List<E> entities, Class<D> clazz) {
		return entities.stream().map(entity -> convert(entity, clazz)).collect(Collectors.toList());
	}

}
