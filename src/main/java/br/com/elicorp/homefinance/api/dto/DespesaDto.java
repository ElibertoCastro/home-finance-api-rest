package br.com.elicorp.homefinance.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DespesaDto {

	private Long id;
	private String descricao;
	private BigDecimal valor;
	private DespesaCategory categoria;
	private LocalDate data_despesa; 
	
}
