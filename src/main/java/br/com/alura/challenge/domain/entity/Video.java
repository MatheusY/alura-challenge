package br.com.alura.challenge.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Where;

import br.com.alura.challenge.constants.WhereConstant;

@Getter
@Setter
@Entity
@Table(name = "TB_VIDEO")
@Where(clause = WhereConstant.ATIVO)
public class Video extends DeletableEntity implements Serializable {

	private static final long serialVersionUID = -5037749762754390237L;

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

	@ManyToOne
	@JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_VIDEO_01"))
	private Categoria categoria;
}
