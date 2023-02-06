package com.teste.apirest.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import com.teste.apirest.entities.Pessoa;
import com.teste.apirest.repositories.EnderecoRepository;
import com.teste.apirest.repositories.PessoaRepository;
import com.teste.apirest.services.PessoaService;
import com.teste.apirest.services.exceptions.DataBaseException;
import com.teste.apirest.services.exceptions.ResourceNotFoundException;

@SpringBootTest
class PessoaServiceTest {

	private static final Long INVALID_ID_PESSOA = 1000L;
	private static final Long ID_PESSOA = 1L;
	private static final String NOME = "Marcos";
	private static final LocalDate DATA_NASCIMENTO = LocalDate.parse("2002-04-26");

	@InjectMocks
	private PessoaService pessoaService;

	@Mock
	private PessoaRepository pessoaRepository;

	@Mock
	private EnderecoRepository enderecoRepository;

	private Pessoa pessoa;
	private Optional<Pessoa> opPessoa;
	private List<Endereco> listEndereco;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		startPessoa();
	}

	@Test
	void whenFindAllReturnAListOfPessoa() {
		Mockito.when(pessoaRepository.findAll()).thenReturn(List.of(pessoa));

		List<Pessoa> listPessoa = pessoaService.findAll();

		Assertions.assertNotNull(listPessoa);
		Assertions.assertEquals(1, listPessoa.size());
		Assertions.assertEquals(Pessoa.class, listPessoa.get(0).getClass());

		Assertions.assertEquals(ID_PESSOA, listPessoa.get(0).getIdPessoa());
		Assertions.assertEquals(NOME, listPessoa.get(0).getNome());
		Assertions.assertEquals(DATA_NASCIMENTO, listPessoa.get(0).getDataNascimento());
	}

	@Test
	void whenFindByIdReturnAnInstanceOfPessoa() {
		Mockito.when(pessoaRepository.findById(Mockito.anyLong())).thenReturn(opPessoa);

		Pessoa response = pessoaService.findById(ID_PESSOA);

		Assertions.assertNotNull(response);
		Assertions.assertEquals(Pessoa.class, response.getClass());
		Assertions.assertEquals(ID_PESSOA, response.getIdPessoa());
		Assertions.assertEquals(NOME, response.getNome());
		Assertions.assertEquals(DATA_NASCIMENTO, response.getDataNascimento());
	}

	@Test
	void whenFindByIdReturnAnResourceNotFoundException() {
		Mockito.when(pessoaRepository.findById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException(ID_PESSOA));
		try {
			pessoaService.findById(ID_PESSOA);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void whenInsertSingleInstanceReturnAnInstanceOfPessoa() {
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoa);

		Pessoa pessoa = pessoaService.insert(this.pessoa);

		Assertions.assertNotNull(pessoa);
		Assertions.assertEquals(Pessoa.class, pessoa.getClass());
		Assertions.assertEquals(ID_PESSOA, pessoa.getIdPessoa());
		Assertions.assertEquals(NOME, pessoa.getNome());
		Assertions.assertEquals(DATA_NASCIMENTO, pessoa.getDataNascimento());
	}

	@Test
	void whenInsertAListOfPessoaReturnAListOfPessoa() {
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoa);

		Set<Pessoa> listPessoa = pessoaService.insertMoreThanOne(List.of(pessoa));

		Assertions.assertNotNull(listPessoa);
		Assertions.assertEquals(1, listPessoa.size());
		Assertions.assertEquals(Pessoa.class, listPessoa.stream().findFirst().get().getClass());
		Assertions.assertEquals(ID_PESSOA, listPessoa.stream().findFirst().get().getIdPessoa());
		Assertions.assertEquals(NOME, listPessoa.stream().findFirst().get().getNome());
		Assertions.assertEquals(DATA_NASCIMENTO, listPessoa.stream().findFirst().get().getDataNascimento());
		Assertions.assertEquals(2, listPessoa.stream().findFirst().get().getEnderecos().size());
	}

	@Test
	void whenInsertPessoaWithoutNomeReturnException() {
		Pessoa pessoaSemNome = pessoa;
		pessoaSemNome.setNome("");
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoaSemNome);
		try {
			pessoaService.insert(pessoaSemNome);
		} catch (Exception e) {
			Assertions.assertEquals(DataBaseException.class, e.getClass());
		}
	}

	@Test
	void whenInsertIntanceOfPessoaWithEnderecoWithoutIdEndereco() {
		List<Endereco> listEnderecoSemId = listEndereco;
		listEndereco.forEach(ender -> {
			ender.setIdEndereco(null);
		});
		Pessoa pessoaComEnderecoSemId = pessoa;
		pessoaComEnderecoSemId.setEnderecos(listEnderecoSemId);
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoaComEnderecoSemId);
		try {
			Pessoa pessoa = pessoaService.insert(pessoaComEnderecoSemId);

			Assertions.assertNotNull(pessoa);
			Assertions.assertEquals(ID_PESSOA, pessoa.getIdPessoa());
			Assertions.assertEquals(NOME, pessoa.getNome());
			Assertions.assertEquals(DATA_NASCIMENTO, pessoa.getDataNascimento());
			Assertions.assertEquals(2, pessoa.getEnderecos().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void whenInsertAlistOfPessoaWithEnderecoWithoutIdEndereco() {
		List<Endereco> listEnderecoSemId = listEndereco;
		listEndereco.forEach(ender -> {
			ender.setIdEndereco(null);
		});
		Pessoa pessoaComEnderecoSemId = pessoa;
		pessoaComEnderecoSemId.setEnderecos(listEnderecoSemId);
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoaComEnderecoSemId);
		try {
			Set<Pessoa> setPessoa = pessoaService.insertMoreThanOne(List.of(pessoaComEnderecoSemId));

			Assertions.assertNotNull(setPessoa);
			Assertions.assertEquals(ID_PESSOA, setPessoa.stream().findFirst().get().getIdPessoa());
			Assertions.assertEquals(NOME, setPessoa.stream().findFirst().get().getNome());
			Assertions.assertEquals(DATA_NASCIMENTO, setPessoa.stream().findFirst().get().getDataNascimento());
			Assertions.assertEquals(2, setPessoa.stream().findFirst().get().getEnderecos().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void whenDeleteAExistingPessoa() {
		Mockito.doNothing().when(pessoaRepository).deleteById(Mockito.anyLong());
		try {
			pessoaService.delete(pessoa.getIdPessoa());
			Mockito.verify(pessoaRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void whenDeleteANonExistingPessoa() {
		Mockito.doNothing().when(pessoaRepository).deleteById(Mockito.anyLong());
		try {
			pessoaService.delete(INVALID_ID_PESSOA);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	@Test
	void whenUpdateSucces() {
		Pessoa pessoaWithoutEndereco = pessoa;
		pessoaWithoutEndereco.setEnderecos(new ArrayList<>());
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoaWithoutEndereco);
		Mockito.when(pessoaRepository.getReferenceById(Mockito.anyLong())).thenReturn(pessoaWithoutEndereco);

		Pessoa pessoa = pessoaService.update(ID_PESSOA, this.pessoa);

		Assertions.assertNotNull(pessoa);
		Assertions.assertEquals(ID_PESSOA, pessoa.getIdPessoa());
		Assertions.assertEquals(NOME, pessoa.getNome());
		Assertions.assertEquals(DATA_NASCIMENTO, pessoa.getDataNascimento());
	}
	
	@Test
	void whenUpdateFailed() {
		Pessoa pessoaWithoutEndereco = pessoa;
		pessoaWithoutEndereco.setEnderecos(new ArrayList<>());
		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoaWithoutEndereco);
		Mockito.when(pessoaRepository.getReferenceById(Mockito.anyLong())).thenReturn(pessoaWithoutEndereco);
		try {
			pessoaService.update(INVALID_ID_PESSOA, pessoaWithoutEndereco);
		} catch (Exception e) {
			Assertions.assertEquals(ResourceNotFoundException.class, e.getClass());
		}
	}

	private void startPessoa() {
		Endereco endereco1 = new Endereco(1L, "Rua teste 1", "8850450", 900, "Lages", false);
		Endereco endereco2 = new Endereco(2L, "Rua teste 2", "8850460", 1000, "Lages", false);

		listEndereco = new ArrayList<>(List.of(endereco1, endereco2));

		pessoa = new Pessoa(ID_PESSOA, NOME, DATA_NASCIMENTO, listEndereco);
		opPessoa = Optional.of(new Pessoa(ID_PESSOA, NOME, DATA_NASCIMENTO, listEndereco));
	}

}
