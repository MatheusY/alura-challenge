package br.com.alura.challenge.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.domain.dto.VideoDTO;
import br.com.alura.challenge.domain.entity.Video;
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

}
