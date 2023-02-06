package com.teste.apirest.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.teste.apirest.entities.Endereco;
import com.teste.apirest.entities.Pessoa;
import com.teste.apirest.repositories.EnderecoRepository;
import com.teste.apirest.repositories.PessoaRepository;

@Configuration
@Profile("dev")
public class TestConfig {

	@Autowired 
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Bean
	public void startDB() {
			
			Endereco end1 = new Endereco(1L, "Rua teste_1", "8850756", 900, "Lages", false);
			Endereco end2 = new Endereco(2L, "Rua teste_2", "8850043", 340, "Camboriu", false);
			Endereco end3 = new Endereco(3L, "Rua teste_3", "8850123", 870, "Florianópolis", false);
			Endereco end4 = new Endereco(4L, "Rua teste_4", "8850543", 2310, "Brusque", false);
			
			Pessoa p1 = new Pessoa(1L, "Fulano", LocalDate.parse("2002-04-26"), Arrays.asList(end1, end2));
			Pessoa p2 = new Pessoa(2L, "Fulano", LocalDate.parse("2000-12-31"), Arrays.asList(end1, end3, end4));
			Pessoa p3 = new Pessoa(3L, "Fulano", LocalDate.parse("2010-04-01"), Arrays.asList(end1, end2));
			Pessoa p4 = new Pessoa(4L, "Fulano", LocalDate.parse("1996-01-01"), null);
			Pessoa p5 = new Pessoa(5L, "Fulano", LocalDate.parse("2002-03-03"), Arrays.asList(end1));
			
			enderecoRepository.saveAll(Arrays.asList(end1, end2, end3, end4));
			pessoaRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5));
			
	}
	
}
