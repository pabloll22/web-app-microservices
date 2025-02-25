package es.uma.jpa;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.*;

import es.uma.jpa.controller.GerenteController;
import es.uma.jpa.dto.GerenteDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.repositories.GerenteRepository;
import es.uma.jpa.security.JwtUtil;
import es.uma.jpa.services.CentroService;
import es.uma.jpa.services.GerenteService;
import lombok.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de gerentes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GerenteTests {
    @Autowired
    @Mock
    private TestRestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    @Mock
    private GerenteRepository gerenteRepository;

    private MockRestServiceServer mockServer;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    public void init() {
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

    private URI uriWithQuery(String scheme,String host, int port, String path,Map<String, Collection<String>> queryParams){
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder().scheme(scheme).host(host).port(port).path(path);
        for(Map.Entry<String, Collection<String>> entry:queryParams.entrySet()){
            ub.queryParam(entry.getKey(),entry.getValue());
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
    @DisplayName("cuando no hay gerentes")
    public class GerentesVacios{

        @Test
    @DisplayName("GET /gerente devuelve 200 y una lista vacía")
    public void getGerentes_NoHayGerentes_Devuelve200YListaVacia() throws URISyntaxException {
        var peticion = get("http", "localhost", port, "/gerente");

        var respuesta = restTemplate.exchange(peticion,
            new ParameterizedTypeReference<Set<Gerente>>() {
            });

        assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
        assertThat(respuesta.getBody()).isEmpty();
    }

        @Test
        @DisplayName("POST /gerente con un gerente válido devuelve 201")
        public void postGerente_GerenteValido_Devuelve201() {
            var gerente = GerenteDTO.builder().id(1L).build();
            var peticion = post("http", "localhost", port, "/gerente", gerente);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(201);
            assertThat(respuesta.getBody()).isEqualTo(gerente);
        
        }

        @Test
        @DisplayName("GET /gerente/{id} devuelve 404")
        public void getGerente_NoHayGerente_Devuelve404() {
            var peticion = get("http", "localhost",port, "/gerente/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<GerenteDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            assertThat(respuesta.hasBody()).isEqualTo(false);
        }

        @Test
        @DisplayName("PUT /gerente/{id} devuelve 404")
        public void putGerente_NoHayGerente_Devuelve404() {
            var gerente = GerenteDTO.builder().id(1L)
                    .build();
            var peticion = put("http", "localhost",port, "/gerente/1", gerente);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("DELETE gerente/{id} devuelve 404")
        public void deleteGerente_NoHayGerente_devuelve404() {
            var peticion = delete("http", "localhost",port, "/gerente/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

    }

    @Nested
    @DisplayName("cuando hay gerentes")
    public class GerentesNoVacios{
        @BeforeEach
        public void insertarDatos() {

            var gerente1 = new Gerente();
            gerente1.setId(1L);
            gerenteRepository.save(gerente1);

            var gerente2 = new Gerente();
            gerente1.setId(2L);
            gerenteRepository.save(gerente2);
        }

        @Test
        @DisplayName("GET /gerente devuelve 200 y una lista de gerentes")
        public void getGerentes_Devuelve200YListaGerentes() throws URISyntaxException {
            var peticion = get("http", "localhost",port, "/gerente");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<GerenteDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(2);
        }

        @Test
        @DisplayName("PUT /gerente{id} devuelve 403")
        public void putGerente_YaExiste_Devuelve403() {

            var gerente = GerenteDTO.builder().id(1L).build();
            var peticion = post("http", "localhost",port, "/gerente", gerente);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion,Void.class);

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Nested
        @DisplayName("GET /gerente{id}")
        public class ObtenerGerente {
            @Test
            @DisplayName("devuelve 200 y un gerente")
            public void getGerente_ExisteGerente_Devuelve200YGerente() {
                var peticion = get("http", "localhost",port, "/gerente/1");

                var respuesta = restTemplate.exchange(peticion, GerenteDTO.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                assertThat(respuesta.hasBody()).isTrue();
                assertThat(respuesta.getBody()).isNotNull();
            }

            @Test
            @DisplayName("devuelve 404")
            public void getGerente_NoExisteGerente_Devuelve404() {
                var peticion = get("http", "localhost",port, "/gerente/999");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<GerenteDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
                assertThat(respuesta.hasBody()).isEqualTo(false);
            }
        }

        @Test
        @DisplayName("PUT /gerente{id} devuelve 200")
        public void putGerente_ExisteGerente_Devuelve200() {
            var gerente = GerenteDTO.builder().id(1L).build();
            var peticion = put("http", "localhost",port, "/gerente/1", gerente);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(gerenteRepository.findById(1L).get().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("DELETE /gerente{id} devuelve 200")
        public void deleteGerente_ExisteGerente_Devuelve200() {
            var peticion = delete("http", "localhost",port, "/gerente/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(gerenteRepository.count()).isEqualTo(1);
        }

    }
}
