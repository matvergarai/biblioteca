package com.biblioteca.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.service.LibroService;
import com.biblioteca.web.dto.LibroResponse;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {

	@Mock
	private LibroService libroService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders.standaloneSetup(new LibroController(libroService))
				.setControllerAdvice(new ManejadorExcepciones())
				.setValidator(validator)
				.build();
	}

	@Test
	void obtieneUnLibroPorId() throws Exception {
		when(libroService.obtenerPorId(1L)).thenReturn(respuesta());

		mockMvc.perform(get("/libros/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.titulo").value("Cien anios de soledad"))
				.andExpect(jsonPath("$.autor").value("Gabriel Garcia Marquez"));
	}

	@Test
	void creaUnLibro() throws Exception {
		when(libroService.crear(any())).thenReturn(respuesta());

		mockMvc.perform(post("/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void actualizaUnLibro() throws Exception {
		when(libroService.actualizar(eq(1L), any())).thenReturn(respuesta());

		mockMvc.perform(put("/libros/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.genero").value("Novela"));
		verify(libroService).actualizar(eq(1L), any());
	}

	@Test
	void eliminaUnLibro() throws Exception {
		mockMvc.perform(delete("/libros/1"))
				.andExpect(status().isNoContent());
		verify(libroService).eliminar(1L);
	}

	@Test
	void responde404SiElLibroNoExiste() throws Exception {
		when(libroService.obtenerPorId(9L))
				.thenThrow(new RecursoNoEncontradoException("Libro no encontrado con id 9"));

		mockMvc.perform(get("/libros/9"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.mensaje").value("Libro no encontrado con id 9"));
	}

	@Test
	void responde400SiFaltanDatos() throws Exception {
		mockMvc.perform(post("/libros")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.titulo").exists());
	}

	@Test
	void eliminarInexistenteResponde404() throws Exception {
		doThrow(new RecursoNoEncontradoException("Libro no encontrado con id 3"))
				.when(libroService).eliminar(3L);

		mockMvc.perform(delete("/libros/3"))
				.andExpect(status().isNotFound());
	}

	private LibroResponse respuesta() {
		return new LibroResponse(1L, "Cien anios de soledad", "Gabriel Garcia Marquez", 1967, "Novela");
	}

	private String cuerpo() {
		return """
				{
				  "titulo": "Cien anios de soledad",
				  "autor": "Gabriel Garcia Marquez",
				  "anioPublicacion": 1967,
				  "genero": "Novela"
				}
				""";
	}

}
