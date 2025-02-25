package es.uma.jpa.controller;

import es.uma.jpa.dto.CentroDTO;
import es.uma.jpa.dto.MensajeNuevoDTO;
import es.uma.jpa.entity.Mensaje;
import es.uma.jpa.exceptions.CentroNoEncontrado;
import es.uma.jpa.exceptions.MensajeNoEncontrado;
import es.uma.jpa.exceptions.ParametroVacio;
import es.uma.jpa.services.MensajeService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mensaje")
public class MensajeController {

    private MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping("/centro")
    public ResponseEntity<List<Mensaje>> getListaMensajesCentro(@RequestParam(value = "centro") Long idCentro) {
        try{
            return ResponseEntity.ok(mensajeService.getListaMensajesCentro(idCentro));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/centro/{idMensaje}")
    public ResponseEntity<Mensaje> getMensajeCentro(@PathVariable Long idMensaje) {
        try{
            return ResponseEntity.ok(mensajeService.getMensaje(idMensaje));
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/centro/{idMensaje}")
    public ResponseEntity<Void> deleteMensajeCentro(@PathVariable Long idMensaje) {
        try{
            mensajeService.deleteMensaje(idMensaje);
            return ResponseEntity.ok().build();
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MensajeNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/centro")
    public ResponseEntity<Mensaje> addMensajeCentro(@RequestParam("centro") Long idCentro,
                                                    @RequestBody MensajeNuevoDTO mensaje,
                                                    UriComponentsBuilder builder) {
        try{
            Mensaje m = mensajeService.addMensajeCentro(idCentro, Mapper.toMensaje(mensaje));
            URI uri = builder
                    .path("/mensaje")
                    .path("/centro")
                    .path(String.format("/%d", m.getIdMensaje()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).body(m);
        } catch (ParametroVacio e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CentroNoEncontrado e) {
            return ResponseEntity.notFound().build();
        }
    }


}
