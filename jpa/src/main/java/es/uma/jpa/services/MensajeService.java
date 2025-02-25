package es.uma.jpa.services;

import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Mensaje;
import es.uma.jpa.exceptions.CentroNoEncontrado;
import es.uma.jpa.exceptions.MensajeNoEncontrado;
import es.uma.jpa.exceptions.ParametroVacio;
import es.uma.jpa.repositories.CentroRepository;
import es.uma.jpa.repositories.MensajeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final CentroRepository centroRepository;

    public MensajeService(MensajeRepository mensajeRepository, CentroRepository centroRepository) {
        this.mensajeRepository = mensajeRepository;
        this.centroRepository = centroRepository;
    }

    public List<Mensaje> getListaMensajesCentro(Long idCentro){
        if(idCentro == null) {
            throw new ParametroVacio();
        }
        Optional<Centro> centro = centroRepository.findById(idCentro);
        if(centro.isPresent()){
            return centro.get().getMensajes();
        } else {
            throw new CentroNoEncontrado();
        }
    }

    public Mensaje getMensaje(Long idMensaje){
        if (idMensaje == null) {
            throw new ParametroVacio();
        }
        Optional<Mensaje> mensaje = mensajeRepository.findById(idMensaje);
        if(mensaje.isPresent()){
            return mensaje.get();
        } else {
            throw new MensajeNoEncontrado();
        }
    }

    public void deleteMensaje(Long idMensaje){
        if (idMensaje == null) {
            throw new ParametroVacio();
        }
        Optional<Mensaje> mensaje = mensajeRepository.findById(idMensaje);
        if(!mensaje.isPresent()){
            throw new MensajeNoEncontrado();
        }
        mensajeRepository.delete(mensaje.get());
    }

    public Mensaje addMensajeCentro(Long idCentro, Mensaje mensaje){
        if (mensaje == null) {
            throw new ParametroVacio();
        }
        Optional<Centro> centroOptional = centroRepository.findById(idCentro);
        if(centroOptional.isPresent()){
            Centro centro = centroOptional.get();
            mensaje.setCentro(centro);
            mensajeRepository.save(mensaje);
            return mensaje;
        }else{
            throw new CentroNoEncontrado();
        }
    }



}