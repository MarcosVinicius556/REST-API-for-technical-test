package com.teste.apirest.services.exceptions;

//Tratamento personalizado de excessões
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	//Passo o id do objeto que eu tentei encontrar
	public ResourceNotFoundException(Object id) {
		super("Resource not found. Id " + id);
	}

}
