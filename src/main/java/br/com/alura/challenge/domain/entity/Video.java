package br.com.alura.challenge.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_VIDEO")
public class Video {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_sequence")
	@SequenceGenerator(name = "video_sequence", sequenceName = "vid_seq")
	private Long id;

	@Column(name = "TITULO", length = 40, nullable = false)
	private String titulo;

	@Column(name = "DESCRICAO", length = 300, nullable = false)
	private String descricao;

	@Column(name = "URL", length = 100, nullable = false)
	private String url;
}
