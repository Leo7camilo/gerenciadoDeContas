package br.com.scrytechapi.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.scrytechapi.event.RecursoCriadoEvent;
import br.com.scrytechapi.exceptionhandler.ScrytechExceptionHandler.Erro;
import br.com.scrytechapi.model.Lancamento;
import br.com.scrytechapi.repository.LancamentoRepository;
import br.com.scrytechapi.repository.filter.LancamentoFilter;
import br.com.scrytechapi.service.LancamentoService;
import br.com.scrytechapi.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	// @Autowired achar uma implementação de alguma classe e injeta na variável
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	/*
	 * inserida para conseguirmos ter acesso ao arquivo de propriedade chamado messages.properties
	 */
	@Autowired
	private MessageSource messageSource;
	
	/*
	 * LancamentoFilter criado para podermos realizar a pesquisa por filtro
	 */
	@GetMapping
	public Page<Lancamento> listar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);

	}
	
	/*
	 * .orElse(null) é porque o método findById retorna um Optinonal e por isso, se
	 * não encontrar nada, retorna null
	 * 
	 * .build() serve para construir a resposta
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@Valid @PathVariable Long codigo) {
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(codigo);

		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
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
	 * Para criar o lancamento, só é necessário informar o código da pessoa e categoria porque o Hiber já faz 
	 * o mapeamento necessário entre as entididas automaticamente
	 * 
	 */
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);

	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	

}
