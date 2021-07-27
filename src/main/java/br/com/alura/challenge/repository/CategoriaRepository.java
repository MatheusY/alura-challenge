package br.com.alura.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.challenge.domain.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Short> {

}
