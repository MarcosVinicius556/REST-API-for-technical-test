package com.teste.apirest.entities.enums;

public enum EnderecoMapMarker {

	
	KEY_ID_PESSOA("idPessoa"), 
	KEY_ENDERECO("endereco");
	
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private EnderecoMapMarker(String key) {
		this.key = key;
	}
	
}
