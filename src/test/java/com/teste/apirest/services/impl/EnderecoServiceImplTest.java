package com.teste.apirest.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.teste.apirest.entities.Endereco;
import com.teste.apirest.entities.enums.EnderecoMapMarker;
import com.teste.apirest.repositories.EnderecoRepository;
import com.teste.apirest.repositories.PessoaRepository;
import com.teste.apirest.services.EnderecoService;
import com.teste.apirest.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class EnderecoServiceImplTest {

	private static final Long ID_PESSOA = 1L;
	private static final Long INVALID_ID_PESSOA = 10000L;

	private static final Long ID_ENDERECO = 1L;
	private static final Long INVALID_ID_ENDERECO = 10000L;
	private static final String LOGRADOURO = "Rua teste 1";
	private static final String CEP = "8899043";
	private static final Integer NUMERO = 100;
	private static final String CIDADE = "Lages";

	@InjectMocks
	private EnderecoService enderecoService;

	@Mock
	private EnderecoRepository enderecoRepository;

	@Mock
	private PessoaRepository pessoaRepository;

	private Endereco endereco;

	private Endereco enderecoPrincipal;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startEndereco();
	}

	@Test
	void whenFindByPessoaReturnAListOfEndereco() {
		Mockito.when(enderecoRepository.findByPessoa(Mockito.anyLong())).thenReturn(Optional.of(Set.of(endereco)));

		Set<Endereco> setEndereco = enderecoService.findAllByPessoa(ID_PESSOA);

		Assertions.assertNotNull(setEndereco);
		Assertions.assertEquals(ID_ENDERECO, setEndereco.stream().findFirst().get().getIdEndereco());
		Assertions.assertEquals(LOGRADOURO, setEndereco.stream().findFirst().get().getLogradouro());
		Assertions.assertEquals(CEP, setEndereco.stream().findFirst().get().getCep());
		Assertions.assertEquals(NUMERO, setEndereco.stream().findFirst().get().getNumero());
		Assertions.assertEquals(CIDADE, setEndereco.stream().findFirst().get().getCidade());
	}

	@Test
	void whenFindByPessoaReturnAListOfEnderecoFailed() {
		Mockito.when(enderecoRepository.findByPessoa(Mockito.anyLong())).thenReturn(Optional.of(Set.of(endereco)));
		try {
			enderecoRepository.findByPessoa(INVALID_ID_PESSOA);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}
	
	@Test
	void whenFindPrincipalByPessoaReturnAInstanceOfEnderecoSucces() {
		Mockito.when(enderecoRepository.findByPessoa(Mockito.anyLong())).thenReturn(Optional.of(Set.of(enderecoPrincipal)));

		Set<Endereco> setEndereco = enderecoService.findAllByPessoa(ID_PESSOA);

		Assertions.assertNotNull(setEndereco);
		Assertions.assertEquals(ID_ENDERECO, setEndereco.stream().findFirst().get().getIdEndereco());
		Assertions.assertEquals(LOGRADOURO, setEndereco.stream().findFirst().get().getLogradouro());
		Assertions.assertEquals(CEP, setEndereco.stream().findFirst().get().getCep());
		Assertions.assertEquals(NUMERO, setEndereco.stream().findFirst().get().getNumero());
		Assertions.assertEquals(CIDADE, setEndereco.stream().findFirst().get().getCidade());
		Assertions.assertEquals(true, setEndereco.stream().findFirst().get().isEnderecoPrincipal());
	}

	@Test
	void whenFindPrincipalByPessoaReturnAInstanceOfEnderecoFailed() {
		Mockito.when(enderecoRepository.findByPessoa(Mockito.anyLong())).thenReturn(Optional.of(Set.of(enderecoPrincipal)));
		try {
			enderecoRepository.findByPessoa(INVALID_ID_PESSOA);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void whenInsertSucces() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);
		Mockito.when(pessoaRepository.existsById(Mockito.anyLong())).thenReturn(true);

		Map<String, Object> mapObjectsToPersist = new HashMap<>();
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ID_PESSOA.getKey(), ID_PESSOA);
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ENDERECO.getKey(), endereco);
		Endereco endereco = enderecoService.insert(mapObjectsToPersist);

		Assertions.assertNotNull(endereco);
		Assertions.assertEquals(Endereco.class, endereco.getClass());
		Assertions.assertEquals(ID_ENDERECO, endereco.getIdEndereco());
		Assertions.assertEquals(LOGRADOURO, endereco.getLogradouro());
		Assertions.assertEquals(CEP, endereco.getCep());
		Assertions.assertEquals(NUMERO, endereco.getNumero());
		Assertions.assertEquals(CIDADE, endereco.getCidade());
	}

	@Test
	void whenInsertFailed() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		Map<String, Object> mapObjectsToPersist = new HashMap<>();
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ID_PESSOA.getKey(), ID_PESSOA);
		mapObjectsToPersist.put(EnderecoMapMarker.KEY_ENDERECO.getKey(), endereco);
		try {
			enderecoService.insert(mapObjectsToPersist);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void whenUpdateSucces() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);
		Mockito.when(enderecoRepository.getReferenceById(Mockito.anyLong())).thenReturn(endereco);

		Endereco endereco = enderecoService.update(ID_ENDERECO, this.endereco);

		Assertions.assertNotNull(endereco);
		Assertions.assertEquals(Endereco.class, endereco.getClass());
		Assertions.assertEquals(ID_ENDERECO, endereco.getIdEndereco());
		Assertions.assertEquals(LOGRADOURO, endereco.getLogradouro());
		Assertions.assertEquals(CEP, endereco.getCep());
		Assertions.assertEquals(NUMERO, endereco.getNumero());
		Assertions.assertEquals(CIDADE, endereco.getCidade());
	}

	@Test
	void whenUpdateFailed() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);
		Mockito.when(enderecoRepository.getReferenceById(Mockito.anyLong())).thenReturn(endereco);
		try {
			enderecoService.update(INVALID_ID_ENDERECO, endereco);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void whenChangeToPrincipalReturnAnInstanceOfEndereco() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(enderecoPrincipal);
		Mockito.when(enderecoRepository.getReferenceById(Mockito.anyLong())).thenReturn(enderecoPrincipal);

		Endereco endereco = enderecoService.update(ID_ENDERECO, this.enderecoPrincipal);

		Assertions.assertNotNull(endereco);
		Assertions.assertEquals(Endereco.class, endereco.getClass());
		Assertions.assertEquals(ID_ENDERECO, endereco.getIdEndereco());
		Assertions.assertEquals(LOGRADOURO, endereco.getLogradouro());
		Assertions.assertEquals(CEP, endereco.getCep());
		Assertions.assertEquals(NUMERO, endereco.getNumero());
		Assertions.assertEquals(CIDADE, endereco.getCidade());
		Assertions.assertEquals(true, endereco.isEnderecoPrincipal());
	}
	
	@Test
	void whenChangeToPrincipalReturnAnInstanceOfEnderecoFailed() {
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(enderecoPrincipal);
		Mockito.when(enderecoRepository.getReferenceById(Mockito.anyLong())).thenReturn(enderecoPrincipal);

		try {
			enderecoService.update(ID_ENDERECO, this.enderecoPrincipal);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void startEndereco() {

		endereco = new Endereco(ID_ENDERECO, LOGRADOURO, CEP, NUMERO, CIDADE, false);

		enderecoPrincipal = new Endereco(ID_ENDERECO, LOGRADOURO, CEP, NUMERO, CIDADE, true);
	}

}
