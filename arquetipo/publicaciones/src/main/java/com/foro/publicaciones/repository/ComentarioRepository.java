package com.foro.publicaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.publicaciones.model.Comentario;
import com.foro.publicaciones.model.EstadoPublicacion;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	List<Comentario> findByTemaIdAndEstadoOrderByFechaCreacionAsc(Long temaId, EstadoPublicacion estado);

	void deleteByTemaId(Long temaId);

}
