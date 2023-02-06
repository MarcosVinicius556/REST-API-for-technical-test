package com.teste.apirest.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.teste.apirest.entities.Pessoa;
import com.teste.apirest.services.PessoaService;

@RestController
@RequestMapping(value="/pessoa")
public class PessoaResources {

	@Autowired
	private PessoaService service;
	
	/**
	 * @return List<Pessoa> pessoas
	 */
	@GetMapping
	public ResponseEntity<List<Pessoa>> findAll() {
		List<Pessoa> listPessoa = service.findAll();
		return ResponseEntity.ok().body(listPessoa);
	}

	/**
	 * @param Long id
	 * @return Pessoa pessoa
	 */
	@GetMapping(value="/{id}")
	public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
		Pessoa pessoa = service.findById(id);
		return ResponseEntity.ok().body(pessoa);
	}
	
	/**
	 * @param Pessoa pessoa
	 * @return Pessoa pessoa inserida no BD
	 */
	@PostMapping
	public ResponseEntity<Pessoa> insert(@RequestBody Pessoa obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
											 .path("/{id}")
											 .buildAndExpand(obj.getIdPessoa())
											 .toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	/**
	 * @apiNote Se for necessário inserir mais de<br>
	 * 	     <t>uma pessoa ao mesmo tempo o sistema<br> 
	 * 		 <t>estaria preparado
	 * @param Set<Pessoa> pessoa
	 * @return Pessoa pessoa inserida no BD
	 */
	@PostMapping(value = "/more")
	public ResponseEntity<Set<Pessoa>> insertMoreThanOne(@RequestBody Pessoa[] objs){
		List<Pessoa> listPessoas = Arrays.asList(objs);
		Set<Pessoa> pessoas = service.insertMoreThanOne(listPessoas);
		return ResponseEntity.ok().body(pessoas);
	}
	
	/**
	 * @param Long id
	 * @return void
	 */
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * @param Long id da pessoa a ser alterada
	 * @param Pessoa pessoa com os dados alterados 
	 * 		  para fazer o merge no banco
	 * @return Pessoa pessoa atualizada no BD
	 */
	@PutMapping(value="/{id}")
	public ResponseEntity<Pessoa> update(@PathVariable Long id, @RequestBody Pessoa obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
