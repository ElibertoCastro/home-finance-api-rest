package br.com.elicorp.homefinance.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.elicorp.homefinance.domain.model.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{

	public List<Receita> findByDescricao(String descricao);
	public List<Receita> findByDataReceita(LocalDate data);

}
