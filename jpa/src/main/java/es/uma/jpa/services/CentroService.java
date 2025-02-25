package es.uma.jpa.services;

import es.uma.jpa.dto.IdGerenteDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.exceptions.*;
import es.uma.jpa.repositories.CentroRepository;
import es.uma.jpa.repositories.GerenteRepository;
import es.uma.jpa.security.SecurityConfiguration;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CentroService {
    private final CentroRepository repo;
    private final GerenteRepository gerenteRepository;

    public CentroService(CentroRepository repo, GerenteRepository gerenteRepository) {
        this.repo = repo;
        this.gerenteRepository = gerenteRepository;
    }

    public List<Centro> getListaCentros(Long idGerente){

        if(idGerente != null){
            List<Centro> centros = repo.findByGerente(idGerente);
            if(centros.isEmpty()){
                throw new GerenteNoEncontrado();
            }
            return centros;
        }
        return repo.findAll();
    }

    public Centro getCentro(Long idCentro){
        if(idCentro==null){
            throw new ParametroVacio();
        }
        Optional<Centro> centro = repo.findById(idCentro);
        if (centro.isPresent()) {
            return centro.get();
        } else {
            throw new CentroNoEncontrado();
        }
    }

    public Centro addCentro(Centro c){
        if(c==null){
            throw new ParametroVacio();
        }
        Optional<Centro> centro = repo.findById(c.getId());
        if (centro.isPresent()) {
            throw new CentroExistente();
        }
        return repo.save(c);
    }

    public void deleteCentro(Long id){
        if(id==null){
            throw new ParametroVacio();
        }
        Optional<Centro> c = repo.findById(id);
        if (c.isPresent()) {
            repo.delete(c.get());
        }else {
            throw new CentroNoEncontrado();
        }
    }

    public Centro modifyCentro(Long id, Centro centro){
        if(centro==null || id==null){
            throw new ParametroVacio();
        }
        Optional<Centro> c = repo.findById(id);

        if (!c.isPresent()) {
            throw new CentroNoEncontrado();
        }
        Centro cMod = c.get();
        cMod.setNombre(centro.getNombre());
        cMod.setLocalizacion(centro.getLocalizacion());
        cMod.setGerente(centro.getGerente());
        return repo.save(cMod);

    }

    public IdGerenteDTO getGerenteCentro(Long id){
        if(id==null){
            throw new ParametroVacio();
        }
        Optional<Centro> centro = repo.findById(id);
        if(centro.isPresent()){
            if(centro.get().getGerente()==null){
                throw new GerenteNoEncontrado();
            }
            return new IdGerenteDTO(centro.get().getGerente().getId());
        }else{
            throw new CentroNoEncontrado();
        }
    }

    // PUT
    public Centro addGerenteACentro(Long idCentro, IdGerenteDTO idGerente){
        if(idCentro==null || idGerente==null){
            throw new ParametroVacio();
        }
        Optional<Centro> centro = repo.findById(idCentro);
        if(!centro.isPresent()) {
            throw new CentroNoEncontrado();
        }
        Optional<Gerente> gerente = gerenteRepository.findById(idGerente.getIdGerente());
        if(!gerente.isPresent()){
            throw new GerenteNoEncontrado();
        }

        centro.get().setGerente(gerente.get());
        return repo.save(centro.get());
    }

    public Centro deleteGerenteDeCentro(Long idCentro, Long idGerente){
        if(idCentro==null){
            throw new ParametroVacio();
        }
        Optional<Centro> centro = repo.findById(idCentro);
        if(!centro.isPresent()) {
            throw new CentroNoEncontrado();
        }
        if (idGerente!=null){
            Gerente gerente = centro.get().getGerente();
            if (gerente.getId().equals(idGerente)){
                centro.get().setGerente(null);
                return repo.save(centro.get());
            } else {
                throw new GerenteNoEncontrado();
            }
        } else {
            centro.get().setGerente(null);
            return repo.save(centro.get());
        }

    }


}
