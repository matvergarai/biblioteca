package com.foro.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.foro.exception.CredencialesInvalidasException;
import com.foro.model.Rol;
import com.foro.service.UsuarioService;
import com.foro.web.dto.UsuarioResponse;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

	@Mock
	private UsuarioService usuarioService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
		mockMvc = MockMvcBuilders.standaloneSetup(new UsuarioController(usuarioService))
				.setControllerAdvice(new ManejadorExcepciones())
				.setValidator(validator)
				.build();
	}

	@Test
	void creaUnUsuario() throws Exception {
		when(usuarioService.crear(any())).thenReturn(respuesta());

		mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.correo").value("ana@foro.cl"))
				.andExpect(jsonPath("$.clave").doesNotExist());
	}

	@Test
	void iniciaSesion() throws Exception {
		when(usuarioService.iniciarSesion(any())).thenReturn(respuesta());

		mockMvc.perform(post("/usuarios/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"correo":"ana@foro.cl","clave":"secreto"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.rol").value("USUARIO"));
	}

	@Test
	void actualizaUnUsuario() throws Exception {
		when(usuarioService.actualizar(eq(1L), any())).thenReturn(respuesta());

		mockMvc.perform(put("/usuarios/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpo()))
				.andExpect(status().isOk());
	}

	@Test
	void eliminaUnUsuario() throws Exception {
		mockMvc.perform(delete("/usuarios/1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void obtieneUnUsuario() throws Exception {
		when(usuarioService.obtenerPorId(1L)).thenReturn(respuesta());

		mockMvc.perform(get("/usuarios/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Ana Perez"));
	}

	@Test
	void responde401SiLaClaveNoCoincide() throws Exception {
		when(usuarioService.iniciarSesion(any()))
				.thenThrow(new CredencialesInvalidasException("Correo o clave incorrectos"));

		mockMvc.perform(post("/usuarios/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"correo":"ana@foro.cl","clave":"mala"}
						"""))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void responde400SiFaltanDatos() throws Exception {
		mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.campos.correo").exists());
	}

	private UsuarioResponse respuesta() {
		return new UsuarioResponse(1L, "Ana Perez", "ana@foro.cl", Rol.USUARIO);
	}

	private String cuerpo() {
		return """
				{
				  "nombre": "Ana Perez",
				  "correo": "ana@foro.cl",
				  "clave": "secreto",
				  "rol": "USUARIO"
				}
				""";
	}

}
