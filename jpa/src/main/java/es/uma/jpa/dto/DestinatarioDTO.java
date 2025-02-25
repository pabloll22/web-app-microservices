package es.uma.jpa.dto;

import es.uma.jpa.entity.TipoDestinatario;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class DestinatarioDTO {
    Long id;
    TipoDestinatario tipo;
}
