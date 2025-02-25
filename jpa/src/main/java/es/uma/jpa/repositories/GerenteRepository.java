package es.uma.jpa.repositories;

import es.uma.jpa.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente,Long> {
    @Override
    Optional<Gerente> findById(Long aLong);

    List<Gerente> findByEmpresa(String empresa);

    Optional<Gerente> findByIdUsuario(Long idUsuario);
}