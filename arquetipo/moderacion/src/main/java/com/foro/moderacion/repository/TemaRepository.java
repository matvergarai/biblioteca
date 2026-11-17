package com.foro.moderacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foro.moderacion.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

	@Query("""
			select t from Tema t
			join fetch t.autor
			order by t.fechaCreacion desc
			""")
	List<Tema> listarConAutor();

}
