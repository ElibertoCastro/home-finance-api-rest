package br.com.elicorp.homefinance.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.elicorp.homefinance.domain.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

	public List<Despesa> findByDescricao(String descricao);
	public List<Despesa> findByDataDespesa(LocalDate data);
}
