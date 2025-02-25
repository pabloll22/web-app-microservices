package es.uma.jpa.entity;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

@Entity
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMensaje;

    private String asunto;

    @ManyToMany
    private List<Destinatario> destinatarios;

    @ManyToMany
    private List<Destinatario> copia;

    @ManyToMany
    private List<Destinatario> copiaOculta;

    @ManyToOne
    private Destinatario remitente;

    private String contenido;

    @ManyToOne
    @JoinColumn(name = "idCentro")
    private Centro centro;
    
    public Mensaje() {
    }

    public Mensaje(String asunto, List<Destinatario> destinatarios, List<Destinatario> copia,
                   List<Destinatario> copiaOculta, Destinatario remitente, String contenido, Centro centro) {
        this.asunto = asunto;
        this.destinatarios = destinatarios;
        this.copia = copia;
        this.copiaOculta = copiaOculta;
        this.remitente = remitente;
        this.contenido = contenido;
        this.centro = centro;
    }

    public Long getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Long idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public List<Destinatario> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<Destinatario> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public List<Destinatario> getCopia() {
        return copia;
    }

    public void setCopia(List<Destinatario> copia) {
        this.copia = copia;
    }

    public List<Destinatario> getCopiaOculta() {
        return copiaOculta;
    }

    public void setCopiaOculta(List<Destinatario> copiaOculta) {
        this.copiaOculta = copiaOculta;
    }

    public Destinatario getRemitente() {
        return remitente;
    }

    public void setRemitente(Destinatario remitente) {
        this.remitente = remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mensaje)) return false;

        Mensaje mensaje = (Mensaje) o;

        return Objects.equals(idMensaje, mensaje.idMensaje);
    }

    @Override
    public int hashCode() {
        return idMensaje != null ? idMensaje.hashCode() : 0;
    }

    @Override   
    public String toString() {
        return "Mensaje{" +
                "idMensaje=" + idMensaje +
                ", asunto='" + asunto + '\'' +
                ", destinatarios=" + destinatarios +
                ", copia=" + copia +
                ", copiaOculta=" + copiaOculta +
                ", remitente=" + remitente +
                ", contenido='" + contenido + '\'' +
                ", centro=" + centro +
                '}';
    }

}