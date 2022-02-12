package br.com.elicorp.homefinance.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReceitaDto {

	private Long id;

	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataReceita;
}
