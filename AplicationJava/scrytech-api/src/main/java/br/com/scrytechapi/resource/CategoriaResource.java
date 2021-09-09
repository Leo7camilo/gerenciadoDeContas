package br.com.scrytechapi.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.scrytechapi.event.RecursoCriadoEvent;
import br.com.scrytechapi.model.Categoria;
import br.com.scrytechapi.repository.CategoriaRepository;

/*
  * Classe que vai expor/controlar tudo os recursos da Categoria
  * 
  * @RestController significa que é um controlador e o "REST" diz que o retorno vai ser Json
  * @RequestMapping está fazendo o mapeamento da requisição
  * 
  * 
  */

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	//@Autowired achar uma implementação de alguma classe e injeta na variável
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
		
	}
	
	
	/*
	 * @RequestBody pega o corpo da requisição
	 * 
	 * uri | fromCurrentRequestUri pega a uri em que foi feita a requisção e adiciona
	 * o código
	 * HttpServletResponse para pegar o Header e setar o Location com a uri da categoria criada
	 * 
	 * ResponseEntity.created retorna o status code 201 para requisção de sucesso e criada
	 * .body(categoriaSalva) retorna a categria que foi criada
	 * 
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse
			response){
		Categoria categoriaSalva = categoriaRepository.save(categoria);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
		
	}
	
	/*
	 * .orElse(null) é porque o método findById retorna um Optinonal
	 * e por isso, se não encontrar nada, retorna null
	 * 
	 * .build() serve para construir a resposta
	 */	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@Valid @PathVariable Long codigo) {
		Optional <Categoria> categoria = this.categoriaRepository.findById(codigo);
		
		return categoria.isPresent() ? 
				ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaRepository.deleteById(codigo);
	}
	

}
