package es.uma.jpa.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class GerenteDTO {
    private Long idUsuario;
    private String empresa;
    private Long id;
}