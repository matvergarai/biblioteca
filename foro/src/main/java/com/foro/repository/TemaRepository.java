package com.foro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foro.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long> {

	@Query("""
			select t from Tema t
			where t.estado = com.foro.model.EstadoPublicacion.PUBLICADO
			  and (:categoriaId is null or t.categoria.id = :categoriaId)
			  and (
			    :texto is null
			    or lower(t.titulo) like lower(concat('%', :texto, '%'))
			    or lower(t.contenido) like lower(concat('%', :texto, '%'))
			  )
			order by t.fechaCreacion desc
			""")
	List<Tema> buscar(@Param("texto") String texto, @Param("categoriaId") Long categoriaId);

}
