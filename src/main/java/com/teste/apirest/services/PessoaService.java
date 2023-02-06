package com.teste.apirest.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.teste.apirest.entities.Endereco;
import com.teste.apirest.entities.Pessoa;
import com.teste.apirest.repositories.EnderecoRepository;
import com.teste.apirest.repositories.PessoaRepository;
import com.teste.apirest.services.exceptions.DataBaseException;
import com.teste.apirest.services.exceptions.ResourceNotFoundException;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	/**
	 * @apiNote findAll --> Retorna todas as pessoas cadastradas
	 * @return List<Pessoa> listPessoa
	 */
	public List<Pessoa> findAll(){
		return pessoaRepository.findAll();
	}
	
	/**
	 * @apiNote FindById --> Busca uma pessoa específica pelo seu ID
	 * @param Long id
	 * @return Pessoa pessoa
	 */
	public Pessoa findById(Long id) {
		Optional<Pessoa> obj = pessoaRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	/**
	 * @apiNote  Insere uma pessoa <br>
	 * 			 <t>Se a pessoa não possuir nome informado, 
	 * 				não será cadastrado
	 *  		 <t>Se tiver endereço informado junto<br> 
	 *			 <t>da pessoa será salvo antes da pessoa<br>
	 *			 <t>Se o endereco informado já possuir registro no banco,<br>
	 *			 <t>irá utilizar o mesmo
	 * @param pessoa
	 * @return Pessoa pessoa
	 * @throws DataBaseException se o nome vier em branco<br> 
	 * 		<t>ou acontecer alguma inconsistencia na hora de inserir no banco
	 */
	public Pessoa insert(Pessoa pessoa) {
		try {
			Optional<List<Endereco>> opEnderecos = Optional.ofNullable(pessoa.getEnderecos());
			opEnderecos.ifPresent(endereco -> {
				endereco.forEach(ender -> {
					Optional<Endereco> opEnderTemp = ender.getIdEndereco() == null 
													 ? Optional.empty() 
													 : enderecoRepository.findById(ender.getIdEndereco());
					if(!opEnderTemp.isPresent()) {
						ender.setIdEndereco(null);
					}
				});
			});
			opEnderecos.ifPresent(enderecoRepository::saveAll);
			if(pessoa.getNome().trim().isEmpty())
				throw new DataBaseException("Nenhum nome informado para o cadastro da pessoa!");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
		return pessoaRepository.save(pessoa);
	}
	
	/**
	 * @apiNote Não insere nenhuma pessoa<br> 
	 * 		    <t>se não estiver tudo correto (Prevenir erros)
	 * 
	 * @param  Set<Pessoa> pessoas a serem inseridas no BD
	 * 
	 * @return Set<Pessoas> inseridas pelo BD <br> 
	 * 		   <t>na operação de INSERT quando houver <br> 
	 * 		   <t>mais de um cadastro informado na mesma <br>
	 * 		   <t>requisição
	 * 
	 * @throws DataBaseException se for identificado um<br> 
	 * 		   <t>cadastro com nome em branco
	 */
	public Set<Pessoa> insertMoreThanOne(List<Pessoa> pessoas){
		Boolean isClienteInvalido = false;
		try {
			isClienteInvalido = pessoas.stream()
									   .filter(pessoa -> pessoa.getNome().isEmpty())
									   .findFirst().isPresent();
			if(!isClienteInvalido) {
				pessoas.forEach(pessoa -> {
					insert(pessoa);
				});
			} else {
				throw new DataBaseException("Uma das pessoas apresenta nome em branco!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
		return pessoas.stream().collect(Collectors.toSet());
	}
	
	/**
	 * @apiNote delete --> Deleta um uma pessoa
	 * @param Long id
	 */
	public void delete(Long id) {
		try {
			pessoaRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) { 
			throw new ResourceNotFoundException(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
	}
	
	/**
	 * @apiNote update --> Salva as alterações da pessoa no banco
	 * @param Pessoa pessoa
	 * @return Pessoa pessoa
	 */	
	public Pessoa update(Long id, Pessoa obj) {
		try {
			Pessoa pessoa = pessoaRepository.getReferenceById(id);
			updateData(pessoa, obj);
			return insert(pessoa);
		} catch (EntityNotFoundException e) { 
			throw new ResourceNotFoundException(id);
		}
	}
	
	/**
	 * @apiNote update --> Altera os dados da pessoa
	 * @param Pessoa pessoa_old
	 * @return Pessoa pessoa_new
	 */
	private void updateData(Pessoa pessoa, Pessoa pessoaRecebido) {
		pessoa.setNome(pessoaRecebido.getNome());
		pessoa.setDataNascimento(pessoaRecebido.getDataNascimento());
		pessoaRecebido.getEnderecos().forEach(pessoa.getEnderecos()::add);
	}
}
