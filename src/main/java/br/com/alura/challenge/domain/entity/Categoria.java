package br.com.alura.challenge.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Where;

import br.com.alura.challenge.constants.WhereConstant;

@Entity
@Table(name = "TB_CATEGORIA", //
		uniqueConstraints = { //
				@UniqueConstraint(name = "UK_CATEGORIA_TITULO", columnNames = "titulo") })
@Getter
@Setter
@Where(clause = WhereConstant.ATIVO)
public class Categoria extends DeletableEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_sequence")
	@SequenceGenerator(name = "categoria_sequence", sequenceName = "cat_seq")
	private Short id;

	@Column(name = "TITULO", length = 30, nullable = false)
	private String titulo;

	@Column(name = "COR", length = 6, nullable = false)
	private String cor;

}
