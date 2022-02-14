package br.com.elicorp.homefinance.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

import br.com.elicorp.homefinance.api.dto.DespesaDto;
import br.com.elicorp.homefinance.domain.model.Despesa;
import br.com.elicorp.homefinance.domain.service.DespesaService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/despesas")
public class DespesaController {
	
	private DespesaService despesaService;
	
	@GetMapping
	public List<DespesaDto> getAllDespesa(String descricao) {
		
		List<DespesaDto> despesas = despesaService.listar(descricao);
		
		if(!despesas.isEmpty()) {
			for (DespesaDto despesaDto : despesas) {
				long id = despesaDto.getId();
				despesaDto.add(linkTo(methodOn(DespesaController.class).getOneDespesa(id)).withSelfRel());
			}
		}
		
		return despesas;
		
	}
	
	@GetMapping("/{ano}/{mes}")
	public List<DespesaDto> resumoMensal(@PathVariable Integer ano, @PathVariable Integer mes) {
		
		List<DespesaDto> despesas = despesaService.resumoMensal(ano, mes);
		
		if(!despesas.isEmpty()) {
			for (DespesaDto despesaDto : despesas) {
				long id = despesaDto.getId();
				despesaDto.add(linkTo(methodOn(DespesaController.class).getOneDespesa(id)).withSelfRel());
			}
		}
		
		return despesas;
		
	}
	
	@GetMapping("/{despesaId}")
	public ResponseEntity<DespesaDto> getOneDespesa(@PathVariable Long despesaId) {
		
		if(despesaService.detalhar(despesaId).isPresent()) {

			var despesaDto = new DespesaDto();
			BeanUtils.copyProperties(despesaService.detalhar(despesaId).get(), despesaDto);
			despesaDto.add(linkTo(methodOn(DespesaController.class).getAllDespesa(null)).withRel("Listar Despesas: "));
			
			return ResponseEntity.ok(despesaDto);
		}
		return ResponseEntity.notFound().build();
								
	}
	
	@PostMapping
	public ResponseEntity<DespesaDto> cadastrar(@Valid @RequestBody DespesaDto despesaDto, HttpServletResponse response) {
		
		// Copiando os dados obtidos de DespesaDto para Despesa
		var despesa = new Despesa();
		BeanUtils.copyProperties(despesaDto, despesa);
		despesa = despesaService.cadastrar(despesa);
		
		// Copiando os dados gravados no banco de dados de Despesa para DespesaDto
		BeanUtils.copyProperties(despesa, despesaDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(despesaDto);
		
	}
	
	@PutMapping("/{despesaId}")
	public ResponseEntity<DespesaDto> atualizar( @PathVariable Long despesaId, @Valid @RequestBody DespesaDto despesaDto) {

		Optional<Despesa> despesaOptional = despesaService.findById(despesaId);

		if(despesaOptional.isEmpty()) {

			return ResponseEntity.notFound().build();

		}		

		// Copiando os dados obtidos de DespesaDto para Despesa
		var despesa = new Despesa();
		BeanUtils.copyProperties(despesaDto, despesa);
		despesa.setId(despesaId);

		// Copiando os dados gravados no banco de dados de Despesa para DespesaDto
		BeanUtils.copyProperties(despesaService.atualizar(despesa), despesaDto);

		return ResponseEntity.ok(despesaDto);
	}

	@DeleteMapping("/{despesaId}")
	public ResponseEntity<Void> excluir(@PathVariable Long despesaId) {
		
		Optional<Despesa> despesaOptional = despesaService.findById(despesaId);
		
		if(despesaOptional.isPresent()) {
			
			despesaService.excluir(despesaId);
			return ResponseEntity.noContent().build();
			
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
}
