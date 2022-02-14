package br.com.elicorp.homefinance.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import br.com.elicorp.homefinance.api.dto.ReceitaDto;
import br.com.elicorp.homefinance.api.event.RecursoCriadoEvent;
import br.com.elicorp.homefinance.domain.model.Receita;
import br.com.elicorp.homefinance.domain.service.ReceitaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/receitas")
public class ReceitaController {

	final ReceitaService receitaService;
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<ReceitaDto> getAllReceita(String descricao) {

		List<ReceitaDto> receitas = receitaService.listar(descricao);
		
		if(!receitas.isEmpty()) {
			for (ReceitaDto receitaDto : receitas) {
				long id = receitaDto.getId();
				receitaDto.add(linkTo(methodOn(ReceitaController.class).getOneReceita(id)).withSelfRel());
			}
		}
		
		return receitas;
	}

	@GetMapping("/{ano}/{mes}")
	public List<ReceitaDto> resumoMensal(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		List<ReceitaDto> receitas = receitaService.resumoMensal(ano, mes);
		
		if(!receitas.isEmpty()) {
			for (ReceitaDto receitaDto : receitas) {
				long id = receitaDto.getId();
				receitaDto.add(linkTo(methodOn(ReceitaController.class).getOneReceita(id)).withSelfRel());
			}
		}
		
		return receitas;
		
	}
	
	@GetMapping("/{receitaId}")
	public ResponseEntity<ReceitaDto> getOneReceita(@PathVariable Long receitaId) {

		if(receitaService.detalhar(receitaId).isPresent()) {

			var receitaDto = new ReceitaDto();
			BeanUtils.copyProperties(receitaService.detalhar(receitaId).get(), receitaDto);
			receitaDto.add(linkTo(methodOn(ReceitaController.class).getAllReceita(null)).withRel("Lista de Receitas: "));
			
			return ResponseEntity.ok(receitaDto);
		}

		return ResponseEntity.notFound().build();

	}

	@PostMapping
	public ResponseEntity<ReceitaDto> salvar(@Valid @RequestBody ReceitaDto receitaDto, HttpServletResponse response) {
		
		// Copiando os dados obtidos de ReceitaDto para Receita
		var receita = new Receita();
		BeanUtils.copyProperties(receitaDto, receita);
		receita = receitaService.cadastrar(receita);
		
		// Adiciona o HATEOAS ao header
		publisher.publishEvent(new RecursoCriadoEvent(this, response, receita.getId()));

		// Copiando os dados gravados no banco de dados de Receita para ReceitaDto
		BeanUtils.copyProperties(receita, receitaDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(receitaDto);
	}

	@PutMapping("/{receitaId}")
	public ResponseEntity<ReceitaDto> atualizar(@PathVariable Long receitaId, @Valid @RequestBody ReceitaDto receitaDto) {
		
		Optional<Receita> receitaOptional = receitaService.findById(receitaId);
		
		if(receitaOptional.isEmpty()) {
			
			return ResponseEntity.notFound().build();
		}
		
		// Copiando os dados obtidos de ReceitaDto para Receita
		var receita = new Receita();
		BeanUtils.copyProperties(receitaDto, receita);
		receita.setId(receitaId);
		
		// Copiando os dados gravados no banco de dados de Receita para ReceitaDto
		BeanUtils.copyProperties(receitaService.atualizar(receita), receitaDto);
		
		return ResponseEntity.ok(receitaDto);
		
	}
	
	@DeleteMapping("/{receitaId}")
	public ResponseEntity<Void> deletar(@PathVariable Long receitaId) {
		
		Optional<Receita> receitaOptional = receitaService.findById(receitaId);
		
		if(receitaOptional.isEmpty()) {

			return ResponseEntity.notFound().build();
			
		}

		receitaService.excluir(receitaId);
		return ResponseEntity.noContent().build();

	}


}
