package es.uma.jpa.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.jpa.services.GerenteService;
import es.uma.jpa.dto.GerenteDTO;
import es.uma.jpa.dto.GerenteNuevoDTO;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.exceptions.GerenteExistente;
import es.uma.jpa.exceptions.GerenteNoEncontrado;
import es.uma.jpa.exceptions.ParametroVacio;
import es.uma.jpa.exceptions.UsuarioNoEncontrado;

@RestController
@RequestMapping("/gerente")
public class GerenteController {
    private GerenteService gerenteService;
    
    public GerenteController(GerenteService gerenteService){
        this.gerenteService = gerenteService;
    }

    @GetMapping
    public ResponseEntity<List<GerenteDTO>> getGerentes(){
        return ResponseEntity.ok(gerenteService.getListaGerentes().stream().map(Mapper::toGerenteDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GerenteDTO> getGerente(@PathVariable Long id){
        try{
            return ResponseEntity.ok(Mapper.toGerenteDTO(gerenteService.getGerente(id)));
        } catch (GerenteNoEncontrado e){
            return ResponseEntity.notFound().build();
        } catch (ParametroVacio e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GerenteDTO> putGerente(@PathVariable Long id, @RequestBody GerenteDTO gerenteDTO){
        try{
            Gerente gerente = Mapper.toGerente(gerenteDTO);
            return ResponseEntity.ok(Mapper.toGerenteDTO(gerenteService.modifyGerente(id, gerente)));
        } catch (GerenteNoEncontrado e){
            return ResponseEntity.notFound().build();
        } catch (ParametroVacio e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGerente(@PathVariable Long id){
        try{
            gerenteService.deleteGerente(id);
            return ResponseEntity.ok().build();
        } catch (GerenteNoEncontrado e){
            return ResponseEntity.notFound().build();
        } catch (ParametroVacio e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<GerenteDTO> postGerente(@RequestBody GerenteNuevoDTO gerenteNuevoDTO, UriComponentsBuilder builder){
        try{
            Gerente gerente = gerenteService.addGerente(Mapper.toGerente(gerenteNuevoDTO));
            GerenteDTO gerenteDTO = Mapper.toGerenteDTO(gerente);
            URI uri = builder
                        .path("/gerente")
                        .path(String.format("/%d", gerenteDTO.getId()))
                        .build()
                        .toUri();
            return ResponseEntity.created(uri).body(gerenteDTO);
        } catch (GerenteExistente e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UsuarioNoEncontrado e){
            return ResponseEntity.notFound().build();
        } 
    }

}
