package es.uma.jpa.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class GerenteNuevoDTO {
    private Long idUsuario;
    private String empresa;
}
