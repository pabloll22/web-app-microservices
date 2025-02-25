package es.uma.jpa.controller;

import java.net.URI;
import java.util.List;

import es.uma.jpa.dto.CentroNuevoDTO;
import es.uma.jpa.dto.IdGerenteDTO;
import es.uma.jpa.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uma.jpa.dto.CentroDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.services.CentroService;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/centro")
public class CentroController {

    private CentroService centroService;

    public CentroController(CentroService centroService){
        this.centroService = centroService;
    }

    @GetMapping
    public ResponseEntity<List<Centro>> getCentros(@RequestParam(value = "gerente", required = false) Long idGerente){
        try{
            return ResponseEntity.ok(centroService.getListaCentros(idGerente));
        } catch (GerenteNoEncontrado e){
            return ResponseEntity.notFound().build();
        }
    }

    //TODO: FALTA BAD REQUEST O ACCESO NO AUTORIZADO
    @GetMapping("/{id}")
    public ResponseEntity<Centro> getCentro(@PathVariable("id") Long idCentro){
        try {
            return ResponseEntity.ok(centroService.getCentro(idCentro));
        } catch (ParametroVacio e) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO: FALTA BAD REQUEST O ACCESO NO AUTORIZADO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCentro(@PathVariable("id") Long id){
        try{
            centroService.deleteCentro(id);
            return ResponseEntity.ok().build();
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO: FALTA BAD REQUEST O ACCESO NO AUTORIZADO
    @PutMapping("/{id}")
    public ResponseEntity<Centro> modifyCentro(@PathVariable("id") Long id, @RequestBody CentroDTO centroDTO){
        try{
            return ResponseEntity.ok(centroService.modifyCentro(id, Mapper.toCentro(centroDTO)));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idCentro}/gerente")
    public ResponseEntity<IdGerenteDTO> getGerenteCentro(@PathVariable("idCentro") Long idCentro){
        try{
            return ResponseEntity.ok(centroService.getGerenteCentro(idCentro));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado | GerenteNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idCentro}/gerente")
    public ResponseEntity<Centro> modifyGerenteCentro(@PathVariable("idCentro") Long idCentro, @RequestBody IdGerenteDTO idGerenteDTO){
        try{
            return ResponseEntity.ok(centroService.addGerenteACentro(idCentro, idGerenteDTO));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado | GerenteNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idCentro}/gerente")
    public ResponseEntity<Centro> deleteGerenteDeCentro(@PathVariable("idCentro") Long idCentro,
                                                        @RequestParam(value = "gerente", required = false) Long idGerente) {
        try{
            return ResponseEntity.ok(centroService.deleteGerenteDeCentro(idCentro, idGerente));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado | GerenteNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO: FALTA NOT FOUND
    @PostMapping()
    public ResponseEntity<Centro> addCentro(@RequestBody CentroNuevoDTO centroDTO,
                                            UriComponentsBuilder builder){
        try{
            Centro c = centroService.addCentro(Mapper.toCentro(centroDTO));
            URI uri = builder
                    .path("/centro")
                    .path(String.format("/%d", c.getId()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).body(c);
        } catch (ParametroVacio e) {
            return ResponseEntity.badRequest().build();
        } catch (CentroExistente e) {
            return ResponseEntity.badRequest().build();
        }
    }


    

}
