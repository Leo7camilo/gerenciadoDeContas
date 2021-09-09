package br.com.scrytechapi.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.scrytechapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
	
}