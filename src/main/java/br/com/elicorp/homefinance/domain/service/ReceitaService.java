package br.com.elicorp.homefinance.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.elicorp.homefinance.domain.model.Receita;
import br.com.elicorp.homefinance.domain.model.exception.NegocioException;
import br.com.elicorp.homefinance.domain.repository.ReceitaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReceitaService {

	private ReceitaRepository receitaRepository;

	public List<Receita> listar(String descricao) {

		if(descricao != null) {

			return receitaRepository.findByDescricao(descricao);

		}

		return receitaRepository.findAll();
	}

	@Transactional
	public Optional<Receita> cadastrar(Receita receita) {

		boolean receitaDuplicada = verificaReceitaJaCadastrada(receita);

		if(!receitaDuplicada) {

			return Optional.of(receitaRepository.save(receita)); 

		}

		throw new NegocioException("Receita já cadastrada para este mês");
	}

	@Transactional
	public Boolean excluir(Long receitaId) {

		if(receitaRepository.existsById(receitaId)) {

			receitaRepository.deleteById(receitaId);
			return true;

		}

		return false;

	}

	@Transactional
	public Optional<Receita> atualizar(Long receitaId, Receita receita) {

		if(receitaRepository.existsById(receitaId)) {

			Receita receitaAntiga = receitaRepository.getById(receitaId);
			receita.setId(receitaId);

			
			if(receitaAntiga.getDescricao().compareTo(receita.getDescricao()) == 0) {
				
				return Optional.of(receitaRepository.save(receita));
				
			} else {
				
				return cadastrar(receita); 				
				
			}
		}
		
		Optional<Receita> receitaOpt = Optional.empty();
		return receitaOpt;

	}

	public ResponseEntity<Receita> detalhar(Long receitaId) {

		return receitaRepository.findById(receitaId)
				.map(receita -> ResponseEntity.ok(receita))
				.orElse(ResponseEntity.notFound().build());
	}


	public List<Receita> resumoMensal(Integer ano, Integer mes) {

		//		filtrar pelo mes;
		List<Receita> receitasPorMes = this.resumoAnual(ano).stream()
				.filter(receita -> receita.getDataReceita().getMonthValue() == mes)
				.collect(Collectors.toList());
		//		retornar lista.
		return receitasPorMes;

	}

	private List<Receita> resumoAnual(Integer ano) {

		//		buscar lista de receitas;
		List<Receita> todasReceitas = receitaRepository.findAll();

		//		filtrar pelo ano;
		List<Receita> receitasPorAno = todasReceitas.stream()
				.filter(receita -> receita.getDataReceita().getYear() == ano)
				.collect(Collectors.toList());

		return receitasPorAno;
	}
	
	private Boolean verificaReceitaJaCadastrada(Receita receita) {
		
		return receitaRepository.findByDescricao(receita.getDescricao())
		.stream()
		.anyMatch(r -> r.verificaReceitaDuplicada(receita));
		
	}
}
