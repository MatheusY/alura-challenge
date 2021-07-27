package br.com.alura.challenge.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.challenge.domain.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Short> {

	@Override
	@Modifying
	@Transactional
	@Query("UPDATE Categoria SET ativo = false WHERE id = :id")
	void deleteById(@Param("id") Short id);

}
