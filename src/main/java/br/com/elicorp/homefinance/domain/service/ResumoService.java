package br.com.elicorp.homefinance.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.elicorp.homefinance.api.dto.ResumoDto;
import br.com.elicorp.homefinance.domain.model.Despesa;
import br.com.elicorp.homefinance.domain.model.Receita;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResumoService {

	private ReceitaService receitaService;
	private DespesaService despesaService;

	public ResumoDto resumoMensal(Integer ano, Integer mes) {

		var resumo = new ResumoDto();

		resumo.setTotalReceitaMes(calculaReceitasMensais(ano, mes));
		resumo.setTotalDespesaMes(calculaDespesasMensais(ano, mes));
		resumo.setSaldoFinalMes(resumo.getTotalReceitaMes().subtract(resumo.getTotalDespesaMes()));

		// Valor gasto por mes por categoria
		calculaValorGastoPorCategoria(ano, mes, resumo);

		// Saldo final do mes: receitas - despesas
		resumo.setSaldoFinalMes(resumo.getTotalReceitaMes().subtract(resumo.getTotalDespesaMes()));

		return resumo;
	}

	private BigDecimal calculaDespesasMensais(Integer ano, Integer mes) {

		// Listar as despesas mensais
		List<Despesa> despesasMensais = despesaService.resumoMensal(ano, mes);

		// Somar as despesas mensais
		Optional<BigDecimal> totalDespesasMensais = despesasMensais
				.stream()
				.map(despesa -> despesa.getValor())
				.reduce((a, b) -> a.add(b));


		if (totalDespesasMensais.isPresent()) {

			return totalDespesasMensais.get();

		}

		return BigDecimal.ZERO;
	}

	private BigDecimal calculaReceitasMensais(Integer ano, Integer mes) {

		// Listar as receitas mensais
		List<Receita> receitasMensais = receitaService.resumoMensal(ano, mes);

		// Somar as receitas mensais
		Optional<BigDecimal> totalReceitasMensais = receitasMensais
				.stream()
				.map(receita -> receita.getValor())
				.reduce((a, b) -> a.add(b));

		if (totalReceitasMensais.isPresent()) {

			return totalReceitasMensais.get();			
		}

		return BigDecimal.ZERO;

	}
	
	private void calculaValorGastoPorCategoria(Integer ano, Integer mes, ResumoDto resumo) {
		
		// Listar as despesas mensais
		List<Despesa> despesasMensais = despesaService.resumoMensal(ano, mes);
		
		if (!despesasMensais.isEmpty()) {

			for (Despesa despesa : despesasMensais) {

				resumo.adicionarCategoriaValor(despesa.getCategoria(), despesa.getValor());

			}

		}
		
	}

}
