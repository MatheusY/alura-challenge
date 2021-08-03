package br.com.alura.challenge.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.domain.dto.CategoriaDTO;
import br.com.alura.challenge.domain.entity.Categoria;
import br.com.alura.challenge.domain.vo.CategoriaVO;
import br.com.alura.challenge.domain.vo.VideoVO;
import br.com.alura.challenge.exception.InvalidKeyException;
import br.com.alura.challenge.service.ICategoriaService;
import br.com.alura.challenge.service.IVideoService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController extends BaseController {

	@Autowired
	private ICategoriaService categoriaService;

	@Autowired
	private IVideoService videoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Short save(@RequestBody @Valid CategoriaDTO categoriaDTO) throws InvalidKeyException {
		Categoria categoria = categoriaService.salvar(convert(categoriaDTO, Categoria.class));
		return categoria.getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Page<CategoriaVO> getAll(Pageable pageable) {
		return convert(categoriaService.buscaTodos(pageable), CategoriaVO.class);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public CategoriaVO getById(@PathVariable Short id) {
		return convert(categoriaService.buscaPorId(id), CategoriaVO.class);
	}

	@GetMapping("/{id}/videos")
	@ResponseStatus(HttpStatus.OK)
	public Page<VideoVO> getVideosByCategoria(@PathVariable Short id, Pageable pageable) {
		return convert(videoService.buscaPorCategoria(id, pageable), VideoVO.class);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Short id, @RequestBody @Valid CategoriaDTO categoriaDTO) throws InvalidKeyException {
		Categoria categoria = convert(categoriaDTO, Categoria.class);
		categoria.setId(id);
		categoriaService.atualiza(categoria);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Short id) {
		categoriaService.apagar(id);
	}
}
