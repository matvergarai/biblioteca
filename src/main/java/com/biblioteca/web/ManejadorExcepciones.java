package com.biblioteca.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biblioteca.exception.ConflictoException;
import com.biblioteca.exception.RecursoNoEncontradoException;
import com.biblioteca.exception.SolicitudInvalidaException;
import com.biblioteca.web.dto.ErrorResponse;

@RestControllerAdvice
public class ManejadorExcepciones {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex) {
		ErrorResponse cuerpo = new ErrorResponse(
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.getReasonPhrase(),
				ex.getMessage(),
				null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
		Map<String, String> campos = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> campos.put(error.getField(), error.getDefaultMessage()));
		ErrorResponse cuerpo = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"Los datos enviados no son validos",
				campos);
		return ResponseEntity.badRequest().body(cuerpo);
	}

	@ExceptionHandler(SolicitudInvalidaException.class)
	public ResponseEntity<ErrorResponse> manejarSolicitudInvalida(SolicitudInvalidaException ex) {
		ErrorResponse cuerpo = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				ex.getMessage(),
				null);
		return ResponseEntity.badRequest().body(cuerpo);
	}

	@ExceptionHandler(ConflictoException.class)
	public ResponseEntity<ErrorResponse> manejarConflicto(ConflictoException ex) {
		ErrorResponse cuerpo = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				ex.getMessage(),
				null);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> manejarIntegridad(DataIntegrityViolationException ex) {
		ErrorResponse cuerpo = new ErrorResponse(
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				"No se puede completar la operacion porque el registro esta relacionado con otros datos",
				null);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
	}

}
