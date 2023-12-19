package com.joseleonardo.lojavirtual.exception;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.joseleonardo.lojavirtual.model.dto.ObjetoErroDTO;

@ControllerAdvice
public class ControleDeExcecoes extends ResponseEntityExceptionHandler {

	/* Captura exceções do projeto */
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, 
			HttpHeaders headers,HttpStatus status, WebRequest request) {
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> objectErrors = ((MethodArgumentNotValidException) ex)
					.getBindingResult()
					.getAllErrors();

			for (ObjectError objectError : objectErrors) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		} else {
			msg = ex.getMessage();
		}

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/* Captura erro na parte do banco de dados */
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, 
		    SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();

		String msg = "";

		if (ex instanceof DataIntegrityViolationException) {
			msg = "Erro de integridade no banco: " +  ((DataIntegrityViolationException) ex)
					.getCause()
					.getCause().getMessage();
		}else if (ex instanceof ConstraintViolationException) {
			msg = "Erro de chave estrangeira: " + ((ConstraintViolationException) ex)
					.getCause()
					.getCause().getMessage();
		}else if (ex instanceof SQLException) {
			msg = "Erro de SQL do Banco: " + ((SQLException) ex)
					.getCause()
					.getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}

		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
