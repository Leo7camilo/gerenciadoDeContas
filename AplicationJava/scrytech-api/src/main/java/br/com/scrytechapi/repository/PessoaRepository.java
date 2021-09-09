package br.com.scrytechapi.repository;


import br.com.scrytechapi.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;


/* JpaRepository é uma inteface que é implentada com vários métodos que nos ajudam
 * no momento de executar os métodos CRUD
 * 
 * Parâmetro é entidade Categoria e o tipo da chave primária é Long
 * 	private Long codigo;
 *
 */

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
