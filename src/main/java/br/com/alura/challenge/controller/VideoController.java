package br.com.alura.challenge.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.alura.challenge.domain.dto.VideoDTO;
import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.domain.vo.VideoVO;
import br.com.alura.challenge.service.IVideoService;

@RestController
@RequestMapping("/video")
public class VideoController extends BaseController {

	@Autowired
	private IVideoService videoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Long save(@RequestBody @Valid VideoDTO videoDto) {
		Video video = videoService.salvar(convert(videoDto, Video.class));
		return video.getId();
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<VideoVO> getAll() {
		return convert(videoService.buscarTodos(), VideoVO.class);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public VideoVO getById(@PathVariable Long id) {
		return convert(videoService.buscarPorId(id), VideoVO.class);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Long id, @RequestBody @Valid VideoDTO videoDTO) {
		Video video = convert(videoDTO, Video.class);
		video.setId(id);
		videoService.atualiza(video);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		videoService.apagar(id);
	}

}
