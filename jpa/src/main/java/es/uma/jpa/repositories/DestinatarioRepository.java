package es.uma.jpa.repositories;

import es.uma.jpa.entity.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Long> {

    @Override
    Optional<Destinatario> findById(Long aLong);

    List<Destinatario> findByTipo(String tipo);

}
