package br.com.elicorp.homefinance.api.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.elicorp.homefinance.api.event.RecursoCriadoEvent;
import br.com.elicorp.homefinance.domain.model.Despesa;
import br.com.elicorp.homefinance.domain.service.DespesaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/despesas")
public class DespesaController {
	
	private DespesaService despesaService;
	private ApplicationEventPublisher publisher; 
	
	@GetMapping
	public List<Despesa> listar(String descricao) {
		
		return despesaService.listar(descricao);
		
	}
	
	@GetMapping("/{ano}/{mes}")
	public List<Despesa> resumoMensal(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		return despesaService.resumoMensal(ano, mes);
		
	}
	
	@GetMapping("/{despesaId}")
	public ResponseEntity<Despesa> detalhar(@PathVariable Long despesaId) {
		
		return despesaService.detalhar(despesaId);
								
	}
	
	@PostMapping
	public ResponseEntity<Despesa> cadastrar(@Valid @RequestBody Despesa despesa, HttpServletResponse response) {
		
		despesa = despesaService.cadastrar(despesa).get();
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, despesa.getId()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(despesa);
		
	}
	
	@PutMapping("/{despesaId}")
	public ResponseEntity<Despesa> atualizar( @PathVariable Long despesaId, @Valid @RequestBody Despesa despesa) {
		
		Optional<Despesa> despesaOpt = despesaService.atualizar(despesaId, despesa);
		
		if(despesaOpt.isPresent()) {

			return ResponseEntity.ok(despesa);
			
		}		
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{despesaId}")
	public ResponseEntity<Void> excluir(@PathVariable Long despesaId) {
		
		if(despesaService.excluir(despesaId)) {
			
			return ResponseEntity.noContent().build();
			
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
}
