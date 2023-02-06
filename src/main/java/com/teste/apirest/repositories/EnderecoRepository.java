package com.teste.apirest.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teste.apirest.entities.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{

	@Query("FROM Endereco ender INNER JOIN ender.pessoas p WHERE p.idPessoa = :idPessoa")
	public Optional<Set<Endereco>> findByPessoa(Long idPessoa);
	
	@Query("FROM Endereco ender INNER JOIN ender.pessoas p WHERE p.idPessoa = :idPessoa AND ender.enderecoPrincipal = true")
	public Optional<Endereco> findPrincipalByPessoa(Long idPessoa);
	
}
