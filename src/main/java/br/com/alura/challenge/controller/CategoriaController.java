package br.com.alura.challenge.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.domain.dto.CategoriaDTO;
import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.service.ICategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends BaseController {

	@Autowired
	private ICategoriaService categoriaService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Short save(@RequestBody @Valid CategoriaDTO categoriaDTO) throws InvalidKeyException {
		Categoria categoria = categoriaService.salvar(convert(categoriaDTO, Categoria.class));
		return categoria.getId();
	}
}
