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
import br.com.elicorp.homefinance.domain.model.Receita;
import br.com.elicorp.homefinance.domain.service.ReceitaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/receitas")
public class ReceitaController {

	private ReceitaService receitaService;
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Receita> listar(String descricao) {

		return receitaService.listar(descricao);

	}

	@GetMapping("/{ano}/{mes}")
	public List<Receita> resumoMensal(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		return receitaService.resumoMensal(ano, mes);
		
	}
	
	@GetMapping("/{receitaId}")
	public ResponseEntity<Receita> detalhar(@PathVariable Long receitaId) {

		return receitaService.detalhar(receitaId);

	}

	@PostMapping
	public ResponseEntity<Receita> salvar(@Valid @RequestBody Receita receita, HttpServletResponse response) {

		receita = receitaService.cadastrar(receita).get();
		
		// Adiciona o HATEOAS ao header
		publisher.publishEvent(new RecursoCriadoEvent(this, response, receita.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(receita);
	}

	@DeleteMapping("/{receitaId}")
	public ResponseEntity<Void> deletar(@PathVariable Long receitaId) {
		
		if(receitaService.excluir(receitaId)) {

			return ResponseEntity.noContent().build();

		}

		return ResponseEntity.notFound().build();

	}

	@PutMapping("/{receitaId}")
	public ResponseEntity<Receita> atualizar(@PathVariable Long receitaId, @Valid @RequestBody Receita receita) {

		Optional<Receita> receitaOpt = receitaService.atualizar(receitaId, receita);
		
		if(receitaOpt.isPresent()) {
			
			return ResponseEntity.ok(receita);
		}
		
		return ResponseEntity.notFound().build();

	}

}
