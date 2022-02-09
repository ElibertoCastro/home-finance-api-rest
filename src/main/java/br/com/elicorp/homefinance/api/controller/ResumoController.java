package br.com.elicorp.homefinance.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.elicorp.homefinance.api.dto.ResumoDto;
import br.com.elicorp.homefinance.domain.service.ResumoService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/resumo")
public class ResumoController {
	
	private ResumoService resumoService;

	@GetMapping("/{ano}/{mes}")
	public ResumoDto resumoMensal(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		return resumoService.resumoMensal(ano, mes);
		
	}
}
