package com.foro.moderacion.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.foro.moderacion.exception.AccesoDenegadoException;
import com.foro.moderacion.exception.RecursoNoEncontradoException;
import com.foro.moderacion.web.dto.ErrorResponse;

@RestControllerAdvice
public class ManejadorExcepciones {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(HttpStatus.NOT_FOUND, ex.getMessage(), null));
	}

	@ExceptionHandler(AccesoDenegadoException.class)
	public ResponseEntity<ErrorResponse> manejarAcceso(AccesoDenegadoException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error(HttpStatus.FORBIDDEN, ex.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
		Map<String, String> campos = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(fallo -> campos.put(fallo.getField(), fallo.getDefaultMessage()));
		return ResponseEntity.badRequest()
				.body(error(HttpStatus.BAD_REQUEST, "Los datos enviados no son validos", campos));
	}

	private ErrorResponse error(HttpStatus status, String mensaje, Map<String, String> campos) {
		return new ErrorResponse(status.value(), status.getReasonPhrase(), mensaje, campos);
	}

}
