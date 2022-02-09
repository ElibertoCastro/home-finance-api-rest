package br.com.elicorp.homefinance.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.elicorp.homefinance.domain.model.Despesa;
import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import br.com.elicorp.homefinance.domain.model.exception.NegocioException;
import br.com.elicorp.homefinance.domain.repository.DespesaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DespesaService {

	private DespesaRepository despesaRepository;

	public List<Despesa> listar(String descricao) {

		if(descricao != null) {

			System.out.println(descricao);
			return despesaRepository.findByDescricao(descricao);

		}

		return despesaRepository.findAll();

	}

	@Transactional
	public Optional<Despesa> cadastrar(Despesa despesa) {

		boolean despesaDuplicada = despesaRepository.findByDescricao(despesa.getDescricao())
				.stream()
				.anyMatch(d -> d.verificaDespesaDuplicada(despesa));

		if(!despesaDuplicada) {

			if(despesa.getCategoria() == null) {

				despesa.setCategoria(DespesaCategory.Outros);

			}

			return Optional.of(despesaRepository.save(despesa));

		}

		throw new NegocioException("Despesa já cadastrada para este mês");

	}

	public ResponseEntity<Despesa> detalhar(Long despesaId) {

		return despesaRepository.findById(despesaId)
				.map(despesa -> ResponseEntity.ok(despesa))
				.orElse(ResponseEntity.notFound().build());

	}

	@Transactional
	public Optional<Despesa> atualizar(Long despesaId, Despesa despesa) {

		if(despesaRepository.existsById(despesaId)) {

			Despesa despesaAntiga = despesaRepository.getById(despesaId);
			despesa.setId(despesaId);


			if(despesaAntiga.getDescricao().compareTo(despesa.getDescricao()) == 0) {

				return Optional.of(despesaRepository.save(despesa));

			} else {

				return cadastrar(despesa); 				

			}
		}

		Optional<Despesa> receitaOpt = Optional.empty();
		return receitaOpt;
	}

	@Transactional
	public boolean excluir(Long despesaId) {

		if(despesaRepository.existsById(despesaId)) {
			
			despesaRepository.deleteById(despesaId);
			return true;
			
		}
		
		return false;

	}

	public List<Despesa> resumoMensal(Integer ano, Integer mes) {

		return this.resumoAnual(ano)
				.stream()
				.filter(despesa -> despesa.getDataDespesa().getMonthValue() == mes)
				.collect(Collectors.toList());
	}

	public List<Despesa> resumoAnual(Integer ano) {

		List<Despesa> todasDespesas = despesaRepository.findAll();
		return todasDespesas.stream()
				.filter(despesa -> despesa.getDataDespesa().getYear() == ano)
				.collect(Collectors.toList());

	}
}
