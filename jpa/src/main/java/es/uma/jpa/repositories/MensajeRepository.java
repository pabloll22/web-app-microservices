package es.uma.jpa.repositories;

import es.uma.jpa.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MensajeRepository extends JpaRepository<Mensaje,Long>{
    @Override
    Optional<Mensaje> findById(Long aLong);
    List<Mensaje> findByAsunto(String asunto);
}
