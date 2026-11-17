package com.foro.moderacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foro.moderacion.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	@Query("""
			select c from Comentario c
			join fetch c.autor
			join fetch c.tema
			order by c.fechaCreacion desc
			""")
	List<Comentario> listarConAutor();

}
