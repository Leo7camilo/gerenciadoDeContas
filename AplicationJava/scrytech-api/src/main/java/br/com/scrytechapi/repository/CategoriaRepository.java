package br.com.scrytechapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.scrytechapi.model.Categoria;

 /* JpaRepository é uma inteface que é implentada com vários métodos que nos ajudam
  * no momento de executar os métodos CRUD
  * 
  * Parâmetro é entidade Categoria e o tipo da chave primária é Long
  * 	private Long codigo;
  *
  */

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
	