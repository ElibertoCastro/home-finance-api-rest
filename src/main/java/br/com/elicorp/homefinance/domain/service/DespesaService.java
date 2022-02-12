package br.com.elicorp.homefinance.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.elicorp.homefinance.api.dto.DespesaDto;
import br.com.elicorp.homefinance.domain.model.Despesa;
import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import br.com.elicorp.homefinance.domain.model.exception.NegocioException;
import br.com.elicorp.homefinance.domain.repository.DespesaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DespesaService {

	private DespesaRepository despesaRepository;

	public List<DespesaDto> listar(String descricao) {

		if(descricao != null) {

			System.out.println(descricao);
			return transformaParaListDespesaDto(despesaRepository.findByDescricao(descricao));

		}

		return transformaParaListDespesaDto(despesaRepository.findAll());

	}

	@Transactional
	public Despesa cadastrar(Despesa despesa) {

		boolean despesaDuplicada = despesaRepository.findByDescricao(despesa.getDescricao())
				.stream()
				.anyMatch(d -> d.verificaDespesaDuplicada(despesa));

		if(!despesaDuplicada) {

			if(despesa.getCategoria() == null) {

				despesa.setCategoria(DespesaCategory.Outros);

			}

			return despesaRepository.save(despesa);

		}

		throw new NegocioException("Despesa já cadastrada para este mês");

	}

	public Optional<Despesa> detalhar(Long despesaId) {

		return despesaRepository.findById(despesaId);

	}

	@Transactional
	public Despesa atualizar(Despesa despesa) {

		Despesa despesaAntiga = despesaRepository.getById(despesa.getId());

		if(despesaAntiga.getDescricao().compareTo(despesa.getDescricao()) == 0) {

			return despesaRepository.save(despesa);

		} else {

			return cadastrar(despesa); 				

		}

	}

	@Transactional
	public void excluir(Long despesaId) {

		despesaRepository.deleteById(despesaId);

	}

	public List<DespesaDto> resumoMensal(Integer ano, Integer mes) {

		return transformaParaListDespesaDto(this.resumoAnual(ano)
				.stream()
				.filter(despesa -> despesa.getDataDespesa().getMonthValue() == mes)
				.collect(Collectors.toList()));
	}

	public Optional<Despesa> findById(Long despesaId) {
		
		return despesaRepository.findById(despesaId);
	
	}
	
	private List<DespesaDto> transformaParaListDespesaDto(List<Despesa> despesasList) {
		
		List<DespesaDto> despesasDto = new ArrayList<DespesaDto>();
		
		for (Despesa despesa: despesasList) {
			
			var despesaDto = new DespesaDto();
			BeanUtils.copyProperties(despesa, despesaDto);
			despesasDto.add(despesaDto);
			
		}
		
		return despesasDto;
		
	}
	
	private List<Despesa> resumoAnual(Integer ano) {
		
		List<Despesa> todasDespesas = despesaRepository.findAll();
		return todasDespesas.stream()
				.filter(despesa -> despesa.getDataDespesa().getYear() == ano)
				.collect(Collectors.toList());
		
	}
	
	
}

