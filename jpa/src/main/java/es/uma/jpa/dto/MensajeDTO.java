package es.uma.jpa.dto;
import java.util.List; 

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class MensajeDTO {
    private Long idMensaje;
    private String asunto;
    private List<Long> idDestinatarios;
    private List<Long> idCopias;
    private List<Long> idCopiasOcultas;
    private Long idRemitente;
    private String contenido;
}
