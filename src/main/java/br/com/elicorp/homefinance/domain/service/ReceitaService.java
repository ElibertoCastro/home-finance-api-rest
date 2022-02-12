package br.com.elicorp.homefinance.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.elicorp.homefinance.api.dto.ReceitaDto;
import br.com.elicorp.homefinance.domain.model.Receita;
import br.com.elicorp.homefinance.domain.model.exception.NegocioException;
import br.com.elicorp.homefinance.domain.repository.ReceitaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReceitaService {

	private ReceitaRepository receitaRepository;

	public List<ReceitaDto> listar(String descricao) {

		if(descricao != null) {

			return transformaParaListDto(receitaRepository.findByDescricao(descricao));

		}

		return transformaParaListDto(receitaRepository.findAll());
	}

	public Optional<Receita> detalhar(Long receitaId) {
		
		return receitaRepository.findById(receitaId);
	}

	@Transactional
	public Receita cadastrar(Receita receita) {

		boolean receitaDuplicada = receitaRepository.findByDescricao(receita.getDescricao())
				.stream()
				.anyMatch(r -> r.verificaReceitaDuplicada(receita));

		if(!receitaDuplicada) {

			return receitaRepository.save(receita); 

		}

		throw new NegocioException("Receita já cadastrada para este mês");
	}

	@Transactional
	public void excluir(Long receitaId) {

		receitaRepository.deleteById(receitaId);

	}

	@Transactional
	public Receita atualizar(Receita receita) {

		Receita receitaAntiga = receitaRepository.getById(receita.getId());

		if(receitaAntiga.getDescricao().compareTo(receita.getDescricao()) == 0) {
			
			return receitaRepository.save(receita);
			
		} else {
			
			return cadastrar(receita); 				
			
		}
	
	}



	public List<ReceitaDto> resumoMensal(Integer ano, Integer mes) {

		//		filtrar pelo mes;
		List<Receita> receitasPorMes = this.resumoAnual(ano).stream()
				.filter(receita -> receita.getDataReceita().getMonthValue() == mes)
				.collect(Collectors.toList());
		//		retornar lista.
		return transformaParaListDto(receitasPorMes);

	}

	public Optional<Receita> findById(Long receitaId) {
		
		return receitaRepository.findById(receitaId);
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

	private List<ReceitaDto> transformaParaListDto(List<Receita> receitaList) {
		
		List<ReceitaDto> receitasDto = new ArrayList<ReceitaDto>();
		
		for (Receita receita : receitaList) {
			
			var receitaDto = new ReceitaDto();
			BeanUtils.copyProperties(receita, receitaDto);
			receitasDto.add(receitaDto);
			
		}
		
		return receitasDto;
		
	}

}
