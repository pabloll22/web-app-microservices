package es.uma.jpa.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CentroNuevoDTO {
    private String nombre;
    private String localizacion;
}
