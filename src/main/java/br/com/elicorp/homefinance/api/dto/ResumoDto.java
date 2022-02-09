package br.com.elicorp.homefinance.api.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.elicorp.homefinance.domain.model.category.DespesaCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumoDto {

	private BigDecimal totalReceitaMes;
	private BigDecimal totalDespesaMes;
	private BigDecimal saldoFinalMes;
	private Map<DespesaCategory, BigDecimal> valorMensalPorCategoria = new HashMap<DespesaCategory, BigDecimal>();
	
	
	public void adicionarCategoriaValor(DespesaCategory categoria, BigDecimal valor) {

		if (valorMensalPorCategoria.containsKey(categoria)) {

			valorMensalPorCategoria.computeIfPresent(categoria, (k, v) -> v.add(valor));
			
		} else {

			valorMensalPorCategoria.put(categoria, valor);
			
		}
		
	}
}
