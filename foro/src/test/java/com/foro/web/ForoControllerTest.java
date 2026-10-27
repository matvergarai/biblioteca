package com.foro.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.foro.service.ComentarioService;
import com.foro.service.TemaService;
import com.foro.web.dto.ComentarioResponse;
import com.foro.web.dto.TemaDetalleResponse;
import com.foro.web.dto.TemaResponse;

@ExtendWith(MockitoExtension.class)
class ForoControllerTest {

	@Mock
	private TemaService temaService;

	@Mock
	private ComentarioService comentarioService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders
				.standaloneSetup(new TemaController(temaService), new ComentarioController(comentarioService))
				.setControllerAdvice(new ManejadorExcepciones())
				.setValidator(validator)
				.build();
	}

	@Test
	void buscaTemasPorTexto() throws Exception {
		when(temaService.buscar(eq("oracle"), isNull())).thenReturn(List.of(tema()));

		mockMvc.perform(get("/temas").param("texto", "oracle"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].titulo").value("Como conectar Oracle"));
	}

	@Test
	void publicaUnTema() throws Exception {
		when(temaService.crear(any())).thenReturn(tema());

		mockMvc.perform(post("/temas")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "titulo": "Como conectar Oracle",
						  "contenido": "Uso el wallet y JPA",
						  "categoriaId": 1,
						  "autorId": 2
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.estado").value("PUBLICADO"));
	}

	@Test
	void muestraUnTemaConSusComentarios() throws Exception {
		when(temaService.obtenerPorId(10L)).thenReturn(new TemaDetalleResponse(tema(), List.of(comentario())));

		mockMvc.perform(get("/temas/10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tema.titulo").value("Como conectar Oracle"))
				.andExpect(jsonPath("$.comentarios[0].contenido").value("Yo use el alias biblio_low"));
	}

	@Test
	void publicaUnComentario() throws Exception {
		when(comentarioService.crear(eq(10L), any())).thenReturn(comentario());

		mockMvc.perform(post("/temas/10/comentarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"contenido":"Yo use el alias biblio_low","autorId":2}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.temaId").value(10));
	}

	private TemaResponse tema() {
		return new TemaResponse(
				10L,
				"Como conectar Oracle",
				"Uso el wallet y JPA",
				LocalDateTime.of(2026, 10, 27, 16, 0),
				"PUBLICADO",
				1L,
				"Bases de datos",
				2L,
				"Ana Perez");
	}

	private ComentarioResponse comentario() {
		return new ComentarioResponse(
				4L,
				"Yo use el alias biblio_low",
				LocalDateTime.of(2026, 10, 27, 16, 5),
				"PUBLICADO",
				10L,
				2L,
				"Ana Perez");
	}

}
