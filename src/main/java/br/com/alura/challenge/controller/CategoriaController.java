package br.com.alura.challenge.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.domain.dto.CategoriaDTO;
import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.vo.CategoriaVO;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.service.ICategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends BaseController {

	@Autowired
	private ICategoriaService categoriaService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Short save(@RequestBody @Valid CategoriaDTO categoriaDTO) throws InvalidKeyException {
		Categoria categoria = categoriaService.salvar(convert(categoriaDTO, Categoria.class));
		return categoria.getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<CategoriaVO> getAll() {
		return convert(categoriaService.buscaTodos(), CategoriaVO.class);
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public CategoriaVO getById(@PathVariable Short id) {
		return convert(categoriaService.buscaPorId(id), CategoriaVO.class);
	}
}
