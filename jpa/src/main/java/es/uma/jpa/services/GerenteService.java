package es.uma.jpa.services;

import es.uma.jpa.entity.Gerente;
import es.uma.jpa.exceptions.GerenteNoEncontrado;
import es.uma.jpa.exceptions.ParametroVacio;
import es.uma.jpa.exceptions.GerenteExistente;

import java.util.List;
import java.util.Optional;

import es.uma.jpa.exceptions.UsuarioNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uma.jpa.repositories.GerenteRepository;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class GerenteService {

    private final GerenteRepository repo;
	private final RestTemplate restTemplate;


	@Autowired
	public GerenteService(GerenteRepository repo, RestTemplate restTemplate) {
		this.repo=repo;
        this.restTemplate = restTemplate;
    }
	
	public List<Gerente> getListaGerentes() {
		return repo.findAll();
	}

    public Gerente getGerente(Long id) {
		if(id == null){
            throw new ParametroVacio();
        }
        Optional<Gerente> u = repo.findById(id);
		if (u.isPresent()) {
			return u.get();
		} else {
			throw new GerenteNoEncontrado();
		}
	}


    public Gerente modifyGerente(Long id, Gerente gerente) {
		if(gerente == null || id == null){
            throw new ParametroVacio();
        }
        Optional<Gerente> u = repo.findById(id);
		if (!u.isPresent()) {
			throw new GerenteNoEncontrado();
		}
		u.get().setEmpresa(gerente.getEmpresa());
		u.get().setIdUsuario(gerente.getIdUsuario());
		return repo.save(u.get());
	}

    public void deleteGerente(Long id) {
        if(id == null){
            throw new ParametroVacio();
        }
		Optional<Gerente> u = repo.findById(id);
		if (u.isPresent()) {
			repo.delete(u.get());
		} else {
			throw new GerenteNoEncontrado();
		}
	}
	
	//Implementar la comprobacion de si existe el usuario en la API de usuarios
    public Gerente addGerente(Gerente gerente) {
		Optional<Gerente> u = repo.findByIdUsuario(gerente.getIdUsuario());
		if (u.isPresent()) {
			throw new GerenteExistente();
		}
		//Miramos con una peticion HTTP a la API de usuarios si existe el usuario
		ResponseEntity<Optional> res = restTemplate.getForEntity("http://localhost:9001/usuario/"+gerente.getIdUsuario(), Optional.class);

		if(res.getStatusCode().is2xxSuccessful()) {
			return repo.save(gerente);
		} else {
			throw new UsuarioNoEncontrado();
		}
	}
}
