package es.uma.jpa.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UsuarioDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private String password;
    private boolean administrador;
    private Long id;

}
