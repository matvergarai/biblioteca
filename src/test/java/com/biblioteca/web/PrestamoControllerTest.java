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

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.biblioteca.exception.ConflictoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.web.dto.PrestamoResponse;

@ExtendWith(MockitoExtension.class)
class PrestamoControllerTest {

	@Mock
	private PrestamoService prestamoService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders.standaloneSetup(new PrestamoController(prestamoService))
				.setControllerAdvice(new ManejadorExcepciones())
				.setValidator(validator)
				.build();
	}

	@Test
	void obtieneUnPrestamoPorId() throws Exception {
		when(prestamoService.obtenerPorId(10L)).thenReturn(respuesta());

		mockMvc.perform(get("/prestamos/10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.prestatario").value("Ana Perez"))
				.andExpect(jsonPath("$.estado").value("ACTIVO"));
	}

	@Test
	void creaUnPrestamo() throws Exception {
		when(prestamoService.crear(any())).thenReturn(respuesta());

		mockMvc.perform(post("/prestamos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.libroId").value(1));
	}

	@Test
	void actualizaUnPrestamo() throws Exception {
		when(prestamoService.actualizar(eq(10L), any())).thenReturn(respuesta());

		mockMvc.perform(put("/prestamos/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isOk());
		verify(prestamoService).actualizar(eq(10L), any());
	}

	@Test
	void eliminaUnPrestamo() throws Exception {
		mockMvc.perform(delete("/prestamos/10"))
				.andExpect(status().isNoContent());
		verify(prestamoService).eliminar(10L);
	}

	@Test
	void responde404SiElPrestamoNoExiste() throws Exception {
		when(prestamoService.obtenerPorId(9L))
				.thenThrow(new RecursoNoEncontradoException("Prestamo no encontrado con id 9"));

		mockMvc.perform(get("/prestamos/9"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.mensaje").value("Prestamo no encontrado con id 9"));
	}

	@Test
	void responde409SiElLibroYaEstaPrestado() throws Exception {
		when(prestamoService.crear(any()))
				.thenThrow(new ConflictoException("El libro ya tiene un prestamo activo"));

		mockMvc.perform(post("/prestamos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensaje").value("El libro ya tiene un prestamo activo"));
	}

	@Test
	void responde400SiFaltanDatos() throws Exception {
		mockMvc.perform(post("/prestamos")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.libroId").exists())
				.andExpect(jsonPath("$.campos.prestatario").exists());
	}

	@Test
	void eliminarInexistenteResponde404() throws Exception {
		doThrow(new RecursoNoEncontradoException("Prestamo no encontrado con id 3"))
				.when(prestamoService).eliminar(3L);

		mockMvc.perform(delete("/prestamos/3"))
				.andExpect(status().isNotFound());
	}

	private PrestamoResponse respuesta() {
		return new PrestamoResponse(
				10L,
				1L,
				"Cien anios de soledad",
				"Ana Perez",
				LocalDate.of(2026, 10, 21),
				null,
				"ACTIVO");
	}

	private String cuerpo() {
		return """
				{
				  "libroId": 1,
				  "prestatario": "Ana Perez",
				  "fechaPrestamo": "2026-10-21"
				}
				""";
	}

}
