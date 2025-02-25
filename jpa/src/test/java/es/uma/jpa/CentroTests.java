package es.uma.jpa;
import es.uma.jpa.dto.CentroDTO;
import es.uma.jpa.dto.CentroNuevoDTO;
import es.uma.jpa.dto.IdGerenteDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.repositories.CentroRepository;
import es.uma.jpa.repositories.GerenteRepository;
import es.uma.jpa.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test de integración de Centro")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CentroTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private CentroRepository centroRepository;

    @Autowired
    private GerenteRepository gerenteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    public void initializeDatabase() {
        centroRepository.deleteAll();
        gerenteRepository.deleteAll();

        token = jwtUtil.generateToken("1");

    }

    private URI uri(String scheme, String host, int port, String... paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path : paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.get(uri)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.delete(uri)
                .header("Authorization", "Bearer " + token)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.post(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.put(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    @Nested
    @DisplayName("Cuando no hay centros")
    public class cuandoNoHayCentros{

        @Test
        @DisplayName("GET /centro devuelve 200 y una lista vacía")
        public void getCentros_NoHayCentros_Devuelve200YListaVacia() {
            var peticion = get("http", "localhost", port, "/centro");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Centro>>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("GET /centro/{idCentro} devuelve 404")
        public void getCentro_NoHayCentros_Devuelve404() {
            var peticion = get("http", "localhost", port, "/centro/1");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("DELETE /centro/{idCentro} devuelve 404")
        public void deleteCentro_NoHayCentros_Devuelve404() {
            var peticion = delete("http", "localhost", port, "/centro/1");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Void>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("PUT /centro/{idCentro} devuelve 404")
        public void putCentro_NoHayCentros_Devuelve404() {
            var peticion = put("http", "localhost", port, "/centro/1", new Centro());

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("GET centro/{idCentro}/gerente devuelve 404")
        public void getGerenteCentro_NoHayCentros_Devuelve404() {
            var peticion = get("http", "localhost", port, "/centro/1/gerente");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<IdGerenteDTO>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("PUT centro/{idCentro}/gerente devuelve 404")
        public void putGerenteCentro_NoHayCentros_Devuelve404() {
            var peticion = put("http", "localhost", port, "/centro/1/gerente", new IdGerenteDTO());

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("DELETE centro/{idCentro}/gerente devuelve 404")
        public void deleteGerenteCentro_NoHayCentros_Devuelve404() {
            var peticion = delete("http", "localhost", port, "/centro/1/gerente");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(404);
        }

        @Test
        @DisplayName("POST /centro con un centro válido devuelve 201")
        public void postCentro_CentroValido_Devuelve201() {

            var centro = CentroNuevoDTO.builder().nombre("Centro 1").localizacion("A Coruña").build();

            var peticion = post("http", "localhost", port, "/centro", centro);

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(201);
            assertThat(respuesta.getBody()).isEqualTo(centro);
        }

    }

    @Nested
    @DisplayName("Cuando hay centros con gerentes")
    public class cuandoHayCentrosConGerentes{

        @BeforeEach
        public void setup() {
            var centro1 = Centro.builder().nombre("Centro 1").localizacion("A Coruña").build();
            var centro2 = Centro.builder().nombre("Centro 2").localizacion("A Coruña").build();
            var centro3 = Centro.builder().nombre("Centro 3").localizacion("A Coruña").build();

            var gerente1 = Gerente.builder().empresa("Empresa 1").idUsuario(1L).build();
            var gerente2 = Gerente.builder().empresa("Empresa 2").idUsuario(2L).build();
            var gerente3 = Gerente.builder().empresa("Empresa 3").idUsuario(3L).build();

            centro1.setGerente(gerente1);
            centro2.setGerente(gerente2);
            centro3.setGerente(gerente3);

            gerenteRepository.saveAll(List.of(gerente1, gerente2, gerente3));
            centroRepository.saveAll(List.of(centro1, centro2, centro3));

        }

        @Test
        @DisplayName("GET /centro con param idGerente devuelve 200 y la lista de centros del gerente")
        public void getCentros_IdGerente_Devuelve200YListaCentros() {
            var peticion = get("http", "localhost", port, "/centro?gerente=1");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Centro>>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(1);
            assertThat(respuesta.getBody().get(0).getNombre()).isEqualTo("Centro 1");
        }

        @Test
        @DisplayName("GET /centro/{idCentro}/gerente devuelve 200 y el gerente del centro")
        public void getGerenteCentro_HayGerente_Devuelve200YGerente() {
            var peticion = get("http", "localhost", port, "/centro/2/gerente");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<IdGerenteDTO>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody().getIdGerente()).isEqualTo(2L);
        }

        @Test
        @DisplayName("PUT /centro/{idCentro}/gerente idGerente devuelve 200 y el centro con el nuevo gerente")
        public void putGerenteCentro_HayGerente_Devuelve200YCentroConNuevoGerente() {
            var peticion = put("http", "localhost", port, "/centro/2/gerente", new IdGerenteDTO(3L));

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Centro 2");
            assertThat(respuesta.getBody().getGerente().getIdUsuario()).isEqualTo(3L);
        }

        @Test
        @DisplayName("DELETE /centro/{idCentro}/gerente devuelve 200 y el centro sin gerente")
        public void deleteGerenteCentro_HayGerente_Devuelve200YCentroSinGerente() {
            var peticion = delete("http", "localhost", port, "/centro/2/gerente");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<Centro>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Centro 2");
            assertThat(respuesta.getBody().getGerente()).isNull();
        }


    }

    //PARTE ALVARO

    @Nested
    @DisplayName("cuando hay centros")
    class CuandoHayCentros {

        @BeforeEach
        public void createCentros() {
            var centro = Centro.builder().nombre("Vals").localizacion("Calle Falsa 123").build();
            var centro2 = Centro.builder().nombre("Vals2").localizacion("Calle Falsa 124").build();
            var centro3 = Centro.builder().nombre("Vals3").localizacion("Calle Falsa 125").build();
            centroRepository.save(centro);
            centroRepository.save(centro2);
            centroRepository.save(centro3);

        }

        @Test
        @DisplayName("GET /centro devuelve 200 y una lista de centros")
        public void getCentros_HayCentros_Devuelve200YListaCentros() {
            var peticion = get("http", "localhost", port, "/centro");
            var response = restTemplate.exchange(
                    peticion,
                    new ParameterizedTypeReference<List<Centro>>() {});
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).hasSize(3);
        }

        @Test
        @DisplayName("GET /centro/{idCentro} devuelve 200 y un centro")
        public void getCentro_HayCentro_Devuelve200YCentro() {
            var peticion = get("http", "localhost", port, "/centro/1");
            var response = restTemplate.exchange(
                    peticion,
                    new ParameterizedTypeReference<Centro>() {});
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getNombre()).isEqualTo("Vals");
        }

        @Test
        @DisplayName("DELETE /centro/{idCentro} devuelve 200")
        public void deleteCentro_HayCentro_Devuelve200() {
            var peticion = delete("http", "localhost", port, "/centro/1");
            restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<Void>() {});
            assertThat(centroRepository.findById(1L)).isEmpty();
            assertThat(centroRepository.count()).isEqualTo(2);
        }

        @Test
        @DisplayName("PUT /centro/{idCentro} devuelve 200")
        public void putCentro_HayCentro_Devuelve200() {
            var centro = Centro.builder().nombre("Vals999").localizacion("Calle Verdadera 999").build();
            var peticion = put("http", "localhost", port, "/centro/1", centro);
            restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<Centro>() {});
            var centroModificado = centroRepository.findById(1L).get();
            assertThat(centroModificado.getNombre()).isEqualTo("Vals999");
            assertThat(centroModificado.getLocalizacion()).isEqualTo("Calle Verdadera 999");
        }

        @Nested
        @DisplayName("sin gerentes")

        class SinGerentes {

            @Test
            @DisplayName("GET /centro con param idGerente devuelve 404")
            public void getCentros_SinGerentes_Devuelve404() {
                var peticion = get("http", "localhost", port, "/centro?gerente=1");
                var response = restTemplate.exchange(
                        peticion,
                        new ParameterizedTypeReference<List<Centro>>() {});
                assertThat(response.getStatusCode().value()).isEqualTo(404);
            }

            @Test
            @DisplayName("GET /centro/{idCentro}/gerente devuelve 404")
            public void getCentroGerente_SinGerentes_Devuelve404() {
                var peticion = get("http", "localhost", port, "/centro/1/gerente");
                var response = restTemplate.exchange(
                        peticion,
                        new ParameterizedTypeReference<IdGerenteDTO>() {});
                assertThat(response.getStatusCode().value()).isEqualTo(404);
            }

            @Test
            @DisplayName("PUT /centro/{idCentro}/gerente devuelve 404")
            public void putCentroGerente_SinGerentes_Devuelve200() {
                var peticion = put("http", "localhost", port, "/centro/1/gerente", 3L);
                var response = restTemplate.exchange(
                        peticion,
                        new ParameterizedTypeReference<Centro>() {});
                assertThat(response.getStatusCode().value()).isEqualTo(404);
            }

            @Test
            @DisplayName("DELETE /centro/{idCentro}/gerente devuelve 200")
            public void deleteCentroGerente_SinGerentes_Devuelve200() {
                var peticion = delete("http", "localhost", port, "/centro/1/gerente");
                var response = restTemplate.exchange(
                        peticion,
                        new ParameterizedTypeReference<Void>() {});
                assertThat(response.getStatusCode().value()).isEqualTo(200);
            }


            @Test
            @DisplayName("POST /centro sin gerente devuelve 201")
            public void postCentro_SinGerente_Devuelve201() {
                var centro = CentroDTO.builder().nombre("Vals35").localizacion("Calle Verdadera 900").build();
                var peticion = post("http", "localhost", port, "/centro", centro);
                var response = restTemplate.exchange(
                        peticion,
                        new ParameterizedTypeReference<Centro>() {});
                assertThat(response.getStatusCode().value()).isEqualTo(201);
                assertThat(response.getBody().getNombre()).isEqualTo("Vals35");
                assertThat(response.getBody().getLocalizacion()).isEqualTo("Calle Verdadera 900");
            }

        }

    }

}

