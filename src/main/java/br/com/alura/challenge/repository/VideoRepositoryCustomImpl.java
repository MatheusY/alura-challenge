package br.com.alura.challenge.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.domain.entity.Video_;

public class VideoRepositoryCustomImpl implements VideoRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Video> findByFiltro(String titulo) {
		CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Video> cQuery = cBuilder.createQuery(Video.class);
		Root<Video> root = cQuery.from(Video.class);

		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(titulo) && !titulo.isBlank()) {
			predicates.add(cBuilder.like(cBuilder.upper(root.get(Video_.titulo)), "%" + titulo.toUpperCase() + "%"));
		}

		cQuery.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<Video> query = entityManager.createQuery(cQuery);

		return query.getResultList();
	}

}
