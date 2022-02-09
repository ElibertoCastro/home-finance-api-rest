package br.com.elicorp.homefinance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity //Sinaliza que é uma entidade para o Jakarta Persistence
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Receita {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String descricao;
	
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	@Column(name = "data_receita")
	private LocalDate dataReceita;
	
	public boolean verificaReceitaDuplicada(Receita receita) {
		 boolean mesmaDescricao = this.descricao.compareTo(receita.getDescricao()) == 0;
		 boolean mesmoMes = this.dataReceita.getMonthValue() == receita.getDataReceita().getMonthValue();
		 
		 return mesmaDescricao == mesmoMes;
	}
}
