package br.com.scrytechapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.scrytechapi.event.RecursoCriadoEvent;
import br.com.scrytechapi.model.Categoria;
import br.com.scrytechapi.model.Pessoa;
import br.com.scrytechapi.repository.PessoaRepository;
import br.com.scrytechapi.service.PessoaService;

/*
 * Classe que vai expor/controlar tudo os recursos da Categoria
 * 
 * @RestController significa que é um controlador e o "REST" diz que o retorno vai ser Json
 * @RequestMapping está fazendo o mapeamento da requisição
 * 
 * 
 */

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	// @Autowired achar uma implementação de alguma classe e injeta na variável
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	// @Autowired achar uma implementação de alguma classe e injeta na variável
	@Autowired
	private PessoaService pessoaService;

	@GetMapping
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();

	}

	/*
	 * @RequestBody pega o corpo da requisição
	 * 
	 * uri | fromCurrentRequestUri pega a uri em que foi feita a requisção e
	 * adiciona o código HttpServletResponse para pegar o Header e setar o Location
	 * com a uri da categoria criada
	 * 
	 * ResponseEntity.created retorna o status code 201 para requisção de sucesso e
	 * criada .body(categoriaSalva) retorna a categria que foi criada
	 * 
	 */
	@PostMapping
	public ResponseEntity<Pessoa> criar(@RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);

	}

	/*
	 * .orElse(null) é porque o método findById retorna um Optinonal e por isso, se
	 * não encontrar nada, retorna null
	 * 
	 * .build() serve para construir a resposta
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPeloCodigo(@Valid @PathVariable Long codigo) {
		Optional<Pessoa> pessoa = this.pessoaRepository.findById(codigo);

		return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.deleteById(codigo);
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeCodigo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}
	
	

}
