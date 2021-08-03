package br.com.alura.challenge.controller;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public abstract class BaseController {

	@Autowired
	private ModelMapper modelMapper;

	protected <E, D> D convert(final E entity, final Class<D> clazz) {
		return modelMapper.map(entity, clazz);
	}

	protected <E, D> Page<D> convert(final Page<E> page, Class<D> clazz) {
		return new PageImpl<>(
				page.getContent().stream().map(entity -> convert(entity, clazz)).collect(Collectors.toList()),
				page.getPageable(), page.getTotalElements());
	}

}
