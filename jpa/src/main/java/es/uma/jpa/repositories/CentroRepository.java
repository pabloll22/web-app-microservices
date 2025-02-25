package es.uma.jpa.repositories;

import es.uma.jpa.entity.Centro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CentroRepository extends JpaRepository<Centro,Long> {

    List<Centro> findByLocalizacion(String nombre);

    @Query("SELECT c FROM Centro c WHERE c.gerente.id = :idGerente")
    List<Centro> findByGerente(Long idGerente);
}
