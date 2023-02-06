package com.teste.apirest.services;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.teste.apirest.entities.Endereco;
import com.teste.apirest.entities.enums.EnderecoMapMarker;
import com.teste.apirest.repositories.EnderecoRepository;
import com.teste.apirest.repositories.PessoaRepository;
import com.teste.apirest.services.exceptions.DataBaseException;
import com.teste.apirest.services.exceptions.ResourceNotFoundException;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/**
	 * @apiNote Faz uma busca dos endereços pela pessoa informada 
	 * @param Long id
	 * @return Set<Endereco> enderecos 
	 */
	public Set<Endereco> findAllByPessoa(Long id) {
		Optional<Set<Endereco>> opEnderecos = enderecoRepository.findByPessoa(id);
		opEnderecos.ifPresent(enderecos -> {
			if(enderecos.size() == 0)
				throw new ResourceNotFoundException(id);
		});
		return opEnderecos.get();
	}
	
	/**
	 * @apiNote Faz uma busca dos endereços pela pessoa informada 
	 * @param Long id
	 * @return Set<Endereco> enderecos 
	 */
	public Endereco findPrincipalByPessoa(Long id) {
		Optional<Endereco> opEndereco = enderecoRepository.findPrincipalByPessoa(id);
		return opEndereco.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	/**
	 * @apiNote Verifica se existe pessoa cadastrada no <br>
	 * 			<t>banco com o id informado, se sim permite <br> 
	 * 			<t>salvar o endereço, se não joga uma exceção. <br>
	 * @param Map<Stirng, Object> mapObjectsToPersist
	 * @return Endereco endereco salvo no banco
	 */
	public Endereco insert(Map<String, Object> mapObjectsToPersist) {
		Optional<Endereco> opEndereco = null;
		try {
			opEndereco = Optional.ofNullable((Endereco) mapObjectsToPersist.get(EnderecoMapMarker.KEY_ENDERECO.getKey()));
			opEndereco.ifPresent(ender -> {
				Optional<Long> opIdPessoa = Optional.ofNullable((Long) mapObjectsToPersist.get(EnderecoMapMarker.KEY_ID_PESSOA.getKey()));
				if(opIdPessoa.isPresent() && pessoaRepository.existsById(opIdPessoa.get())) {
					enderecoRepository.save(ender);
				} else {
					throw new ResourceNotFoundException(opIdPessoa.get());
				}
			});
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
		return opEndereco.get();
	}

	/**
	 * @apiNote update --> Salva as alterações do endereco no banco
	 * @param Endereco endereco
	 * @return Endereco endereco
	 */	
	public Endereco update(Long id, Endereco obj) {
		try {
			Endereco endereco = enderecoRepository.getReferenceById(id);
			updateData(endereco, obj);
			return enderecoRepository.save(endereco);
		} catch (EntityNotFoundException e) { 
			throw new ResourceNotFoundException(id);
		}
	}
	
	/**
	 * @apiNote update --> Salva as alterações do endereco no banco
	 * @param Endereco endereco
	 * @return Endereco endereco
	 */	
	public Endereco changeToPrincipal(Long idPessoa, Long idEndereco) {
		Optional<Endereco> opEndereco = null;
		try {
			opEndereco = Optional.ofNullable(enderecoRepository.getReferenceById(idEndereco));
			opEndereco.ifPresent(ender -> {
				if(pessoaRepository.existsById(idPessoa)) {
					ender.setEnderecoPrincipal(true);
				} else {
					throw new ResourceNotFoundException(idPessoa);
				}
			});
			return enderecoRepository.save(opEndereco.get());
		} catch (EntityNotFoundException e) { 
			throw new ResourceNotFoundException(idEndereco);
		}
	}
	
	/**
	 * @apiNote update --> Altera os dados do endereco
	 * @param Endereco endereco_old
	 * @return Endereco endereco_new
	 */
	private void updateData(Endereco endereco, Endereco enderecoRecebido) {
		endereco.setLogradouro(enderecoRecebido.getLogradouro());
		endereco.setCep(enderecoRecebido.getCep());;
		endereco.setNumero(enderecoRecebido.getNumero());
		endereco.setCidade(enderecoRecebido.getCidade());
	}
	
}
