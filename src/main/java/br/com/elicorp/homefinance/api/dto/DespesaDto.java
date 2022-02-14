package br.com.elicorp.homefinance.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;

import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DespesaDto extends RepresentationModel<DespesaDto> implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	private DespesaCategory categoria;
	
	@NotNull
	private LocalDate dataDespesa; 
	
}
