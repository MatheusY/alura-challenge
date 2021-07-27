package br.com.alura.challenge.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alura.challenge.domain.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

	@Override
	@Modifying
	@Transactional
	@Query("UPDATE Video SET ativo = false WHERE id = :id")
	void deleteById(@Param("id") Long id);

	List<Video> findByCategoriaId(Short idCategoria);
}
