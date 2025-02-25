package es.uma.jpa.controller;

import es.uma.jpa.dto.CentroDTO;
import es.uma.jpa.dto.CentroNuevoDTO;
import es.uma.jpa.dto.GerenteDTO;
import es.uma.jpa.dto.GerenteNuevoDTO;
import es.uma.jpa.dto.MensajeDTO;
import es.uma.jpa.dto.MensajeNuevoDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.entity.Mensaje;
import es.uma.jpa.entity.Destinatario;
import es.uma.jpa.repositories.DestinatarioRepository;


import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    private static DestinatarioRepository destinatarioRepository;

    public Mapper(DestinatarioRepository destinatarioRepository) {
        this.destinatarioRepository = destinatarioRepository;
    }

    public static GerenteDTO toGerenteDTO(Gerente gerente){
        GerenteDTO gerenteDTO = new GerenteDTO();
        gerenteDTO.setId(gerente.getId());
        gerenteDTO.setEmpresa(gerente.getEmpresa());
        gerenteDTO.setIdUsuario(gerente.getIdUsuario());
        return gerenteDTO;
    }

    public static Gerente toGerente(GerenteDTO gerenteDTO){
        Gerente gerente = new Gerente();
        gerente.setId(gerenteDTO.getId());
        gerente.setEmpresa(gerenteDTO.getEmpresa());
        gerente.setIdUsuario(gerenteDTO.getIdUsuario());
        return gerente;
    }

    public static Gerente toGerente(GerenteNuevoDTO gerenteNuevoDTO){
        Gerente gerente = new Gerente();
        gerente.setEmpresa(gerenteNuevoDTO.getEmpresa());
        gerente.setIdUsuario(gerenteNuevoDTO.getIdUsuario());
        return gerente;
    }

    public static CentroDTO toCentroDTO(Centro centro){
        CentroDTO centroDTO = new CentroDTO();
        centroDTO.setId(centro.getId());
        centroDTO.setNombre(centro.getNombre());
        centroDTO.setLocalizacion(centro.getLocalizacion());
        return centroDTO;
    }

    public static Centro toCentro(CentroDTO centroDTO){
        Centro centro = new Centro();
        centro.setId(centroDTO.getId());
        centro.setNombre(centroDTO.getNombre());
        centro.setLocalizacion(centroDTO.getLocalizacion());
        return centro;
    }

    public static Centro toCentro(CentroNuevoDTO centroNuevoDTO){
        Centro centro = new Centro();
        centro.setNombre(centroNuevoDTO.getNombre());
        centro.setLocalizacion(centroNuevoDTO.getLocalizacion());
        return centro;
    }

    public static MensajeDTO toMensajeDTO(Mensaje mensaje){
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setIdMensaje(mensaje.getIdMensaje());
        mensajeDTO.setAsunto(mensaje.getAsunto());
        mensajeDTO.setContenido(mensaje.getContenido());

        // Guardar lista destinatarios con sus IDs
        mensajeDTO.setIdDestinatarios(mensaje.getDestinatarios().stream()
                .map(Destinatario::getId)
                .collect(Collectors.toList()));

        // Guardar lista copias con sus IDs
        mensajeDTO.setIdCopias(mensaje.getCopia().stream()
                .map(Destinatario::getId)
                .collect(Collectors.toList()));

        // Guardar lista copias ocultas con sus IDs
        mensajeDTO.setIdCopiasOcultas(mensaje.getCopiaOculta().stream()
                .map(Destinatario::getId)
                .collect(Collectors.toList()));

        // Mapear remitente a su ID
        if (mensaje.getRemitente() != null) {
            mensajeDTO.setIdRemitente(mensaje.getRemitente().getId());
        }

        return mensajeDTO;
    }

    public static Mensaje toMensaje(MensajeDTO mensajeDTO) {
        if (mensajeDTO == null) {
            return null;
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setIdMensaje(mensajeDTO.getIdMensaje()); // si necesitas setear el ID, dependiendo del caso
        mensaje.setAsunto(mensajeDTO.getAsunto());
        mensaje.setContenido(mensajeDTO.getContenido());

        // Convertir listas de IDs a listas de entidades Destinatario
        if (mensajeDTO.getIdDestinatarios() != null && !mensajeDTO.getIdDestinatarios().isEmpty()) {
            List<Destinatario> destinatarios = destinatarioRepository.findAllById(mensajeDTO.getIdDestinatarios());
            mensaje.setDestinatarios(destinatarios);
        }

        if (mensajeDTO.getIdCopias() != null && !mensajeDTO.getIdCopias().isEmpty()) {
            List<Destinatario> copias = destinatarioRepository.findAllById(mensajeDTO.getIdCopias());
            mensaje.setCopia(copias);
        }

        if (mensajeDTO.getIdCopiasOcultas() != null && !mensajeDTO.getIdCopiasOcultas().isEmpty()) {
            List<Destinatario> copiasOcultas = destinatarioRepository.findAllById(mensajeDTO.getIdCopiasOcultas());
            mensaje.setCopiaOculta(copiasOcultas);
        }

        if (mensajeDTO.getIdRemitente() != null) {
            Destinatario remitente = destinatarioRepository.findById(mensajeDTO.getIdRemitente()).orElse(null);
            mensaje.setRemitente(remitente);
        }

        return mensaje;
    }

    public static Mensaje toMensaje(MensajeNuevoDTO mensajeNuevoDTO) {
        if (mensajeNuevoDTO == null) {
            return null;
        }

        Mensaje mensaje = new Mensaje();

        mensaje.setAsunto(mensajeNuevoDTO.getAsunto());
        mensaje.setContenido(mensajeNuevoDTO.getContenido());

        // Convertir listas de IDs a listas de entidades Destinatario
        if (mensajeNuevoDTO.getIdDestinatarios() != null && !mensajeNuevoDTO.getIdDestinatarios().isEmpty()) {
            List<Destinatario> destinatarios = destinatarioRepository.findAllById(mensajeNuevoDTO.getIdDestinatarios());
            mensaje.setDestinatarios(destinatarios);
        }

        if (mensajeNuevoDTO.getIdCopias() != null && !mensajeNuevoDTO.getIdCopias().isEmpty()) {
            List<Destinatario> copias = destinatarioRepository.findAllById(mensajeNuevoDTO.getIdCopias());
            mensaje.setCopia(copias);
        }

        if (mensajeNuevoDTO.getIdCopiasOcultas() != null && !mensajeNuevoDTO.getIdCopiasOcultas().isEmpty()) {
            List<Destinatario> copiasOcultas = destinatarioRepository.findAllById(mensajeNuevoDTO.getIdCopiasOcultas());
            mensaje.setCopiaOculta(copiasOcultas);
        }

        if (mensajeNuevoDTO.getIdRemitente() != null) {
            Destinatario remitente = destinatarioRepository.findById(mensajeNuevoDTO.getIdRemitente()).orElse(null);
            mensaje.setRemitente(remitente);
        }

        return mensaje;
    }
}
