package br.com.elicorp.homefinance.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import lombok.Data;

@Data
public class DespesaDto {

	private Long id;
	
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	private DespesaCategory categoria;
	
	@NotNull
	private LocalDate dataDespesa; 
	
}
