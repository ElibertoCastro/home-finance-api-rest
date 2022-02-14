package br.com.elicorp.homefinance.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceitaDto extends RepresentationModel<ReceitaDto> implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataReceita;
}
