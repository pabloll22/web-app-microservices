package es.uma.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

public class Destinatario{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoDestinatario tipo;

    private Long idUsuario;

    @OneToMany(mappedBy = "remitente")
    private List<Mensaje> mensajes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDestinatario getTipo() {
        return tipo;
    }

    public void setTipo(TipoDestinatario tipo) {
        this.tipo = tipo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Destinatario() {
        this.mensajes = new ArrayList<>();
    }

    public Destinatario(TipoDestinatario tipo, Long idUsuario){
        super();
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Destinatario{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", idUsuario=" + idUsuario +
                ", mensajes=" + (mensajes != null ? mensajes.size() : 0) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Destinatario)) return false;

        Destinatario dest = (Destinatario) o;

        return Objects.equals(id, dest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
