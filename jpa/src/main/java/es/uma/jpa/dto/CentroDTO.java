package es.uma.jpa.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CentroDTO {
    private Long id;
    private String nombre;
    private String localizacion;
    private Long idGerente;
}
