package br.com.alura.challenge.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.alura.challenge.domain.entity.Video;
import br.com.alura.challenge.domain.entity.Video_;

public class VideoRepositoryCustomImpl implements VideoRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<Video> findByFiltro(final String titulo, final Pageable pageable) {
		CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Video> cQuery = cBuilder.createQuery(Video.class);
		Root<Video> root = cQuery.from(Video.class);

		List<Predicate> predicates = createPredicate(titulo, cBuilder, root);
		
		Long count = countByFiltro(titulo);
		if (count == 0) {
			return new PageImpl<>(Collections.emptyList(), pageable, count);
		}

		cQuery.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<Video> query = entityManager.createQuery(cQuery);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		
		return new PageImpl<>(query.getResultList(), pageable, count);
	}

	private Long countByFiltro(final String titulo) {
		CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cQuery = cBuilder.createQuery(Long.class);

		Root<Video> root = cQuery.from(Video.class);
		
		List<Predicate> predicates = createPredicate(titulo, cBuilder, root);

		CriteriaQuery<Long> countSelect = cQuery.select(cBuilder.count(root.get(Video_.ID)));
		countSelect.where(predicates.toArray(new Predicate[predicates.size()]));

		return entityManager.createQuery(countSelect).getSingleResult();
	}
	
	private List<Predicate> createPredicate(final String titulo, CriteriaBuilder cBuilder, Root<Video> root) {
		List<Predicate> predicates = new ArrayList<>();
		if (Objects.nonNull(titulo) && !titulo.isBlank()) {
			predicates.add(cBuilder.like(cBuilder.upper(root.get(Video_.titulo)), "%" + titulo.toUpperCase() + "%"));
		}
		return predicates;
	}

}
