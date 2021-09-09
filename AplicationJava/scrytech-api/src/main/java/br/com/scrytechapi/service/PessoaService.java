package br.com.scrytechapi.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.scrytechapi.model.Pessoa;
import br.com.scrytechapi.repository.PessoaRepository;

@Service
public class PessoaService {

	// @Autowired achar uma implementação de alguma classe e injeta na variável
	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {

		Optional<Pessoa> pessoaSalva = buscarPessoaPeloCodigo(codigo);
		/*
		 * Se pessoa não for encontrada, é lançada a exception NoSuchElementException
		 * que está sendo tratado no método handlerEmptyResultDataAccessException na
		 * classe de Exception
		 */
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return pessoaRepository.save(pessoaSalva.get());

	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Optional<Pessoa> pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.get().setAtivo(ativo);
		pessoaRepository.save(pessoaSalva.get());
	}

	private Optional<Pessoa> buscarPessoaPeloCodigo(Long codigo) {
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
		return pessoaSalva;
	}

}
