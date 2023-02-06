package com.teste.apirest.resources;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.teste.apirest.entities.Endereco;
import com.teste.apirest.entities.enums.EnderecoMapMarker;
import com.teste.apirest.services.EnderecoService;

@RestController
@RequestMapping(value="/endereco")
public class EnderecoResources {

	@Autowired
	private EnderecoService service;
	
	/**
	 * @param Long id
	 * @return Set<Endereco> enderecos
	 */
	@GetMapping(value="/{id}")
	public ResponseEntity<Set<Endereco>> findAllByPessoa(@PathVariable Long id) {
		Set<Endereco> enderecos = service.findAllByPessoa(id);
		return ResponseEntity.ok().body(enderecos);
	}

	/**
	 * @param Long id
	 * @return Endereco endereco
	 */
	@GetMapping(value="/principal/{id}")
	public ResponseEntity<Endereco> findPrincipalByPessoa(@PathVariable Long id) {
		Endereco endereco = service.findPrincipalByPessoa(id);
		return ResponseEntity.ok().body(endereco);
	}
	
	/**
	 * @param Long idPessoa
	 * @param Endereco obj
	 * @return Endereco endereco inserido
	 */
	@PostMapping(value="{idPessoa}")
	public ResponseEntity<Endereco> insert(@PathVariable Long idPessoa, @RequestBody Endereco obj){
		Map<String, Object> mapObjectsToPersist = new HashMap<>();
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ID_PESSOA.getKey(), idPessoa);
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ENDERECO.getKey(), obj);
		
		obj = service.insert(mapObjectsToPersist);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
											 .path("/{id}")
											 .buildAndExpand(obj.getIdEndereco())
											 .toUri(); 
		return ResponseEntity.created(uri).body(obj);
	}
	
	/**
	 * @param Long idPessoa
	 * @param Long idEndereco
	 * @return Endereco endereco atualizado
	 */
	@PutMapping(value="trocarPrincipal/{idPessoa}/{idEndereco}")
	public ResponseEntity<Endereco> changePrincipal(@PathVariable Long idPessoa, @PathVariable Long idEndereco){
		Endereco endereco = service.changeToPrincipal(idPessoa, idEndereco);
		return ResponseEntity.ok().body(endereco);
	}
	
	/**
	 * 
	 * @param Long id
	 * @param Endereco obj
	 * @return Endereco endereco atualizado
	 */
	@PutMapping(value="/{id}")
	public ResponseEntity<Endereco> update(@PathVariable Long id, @RequestBody Endereco obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
