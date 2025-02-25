package es.uma.jpa.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

@Entity
public class Gerente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String empresa;

    private Long idUsuario;

    @OneToMany(mappedBy = "gerente", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Centro> centros;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Centro> getCentros() {
        return centros;
    }

    public void setCentros(List<Centro> centros) {
        this.centros = centros;
    }

    public Gerente() {
        this.centros = new ArrayList<>();
    }

    public Gerente(String empresa, Long idUsuario) {
        super();
        this.empresa = empresa;
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gerente gerente = (Gerente) o;
        return Objects.equals(idUsuario, gerente.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }


    @Override
    public String toString() {
        return "Gerente{" +
                "id=" + id +
                ", empresa='" + empresa + '\'' +
                ", idUsuario=" + idUsuario +
                ", centros=" + (centros != null ? centros.size() : 0) +
                '}';
    }
}
