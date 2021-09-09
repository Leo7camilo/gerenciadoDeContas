package br.com.scrytechapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.scrytechapi.model.Lancamento;
import br.com.scrytechapi.model.Pessoa;
import br.com.scrytechapi.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
