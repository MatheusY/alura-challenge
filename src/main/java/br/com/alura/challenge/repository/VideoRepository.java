package br.com.alura.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.challenge.domain.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}
