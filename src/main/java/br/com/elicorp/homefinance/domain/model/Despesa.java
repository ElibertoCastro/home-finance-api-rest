package br.com.elicorp.homefinance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Despesa {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	private DespesaCategory categoria;
	
	@NotNull
	@Column(name = "data_despesa")
	private LocalDate dataDespesa;
	
	
	public boolean verificaDespesaDuplicada(Despesa despesa) {
		 boolean mesmaDescricao = this.descricao.compareTo(despesa.getDescricao()) == 0;
		 boolean mesmoMes = this.dataDespesa.getMonthValue() == despesa.getDataDespesa().getMonthValue();
		 
		 return mesmaDescricao == mesmoMes;
	}
	
}
