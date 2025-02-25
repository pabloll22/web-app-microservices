package es.uma.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import es.uma.jpa.repositories.CentroRepository;
import es.uma.jpa.repositories.MensajeRepository;
import es.uma.jpa.security.JwtUtil;
import es.uma.jpa.services.MensajeService;
import es.uma.jpa.controller.Mapper;
import es.uma.jpa.controller.MensajeController;
import es.uma.jpa.dto.MensajeDTO;
import es.uma.jpa.dto.MensajeNuevoDTO;
import es.uma.jpa.entity.Centro;
import es.uma.jpa.entity.Destinatario;
import es.uma.jpa.entity.Gerente;
import es.uma.jpa.entity.Mensaje;
import es.uma.jpa.exceptions.CentroNoEncontrado;
import es.uma.jpa.exceptions.ParametroVacio;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de mensajes")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class MensajeTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private MensajeRepository mensajeRepo;

    @Autowired
    private MensajeService mensajeService;

	@Autowired
	private CentroRepository centroRepository;

	@Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    public void initializeDatabase() {
        mensajeRepo.deleteAll();

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

    private void compruebaCampos(MensajeNuevoDTO expected, Mensaje actual) {
		assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
		assertThat(actual.getContenido()).isEqualTo(expected.getContenido());
	}

    @Nested
	@DisplayName("cuando no hay mensajes")
    public class NoHayMensajes {
		@BeforeEach
        public void setup() {
            var centro1 = Centro.builder().nombre("Centro 1").localizacion("A Coruña").build();
            var centro2 = Centro.builder().nombre("Centro 2").localizacion("A Coruña").build();
            var centro3 = Centro.builder().nombre("Centro 3").localizacion("A Coruña").build();
            centroRepository.saveAll(List.of(centro1, centro2, centro3));

        }

        @Test
        @DisplayName("devuelve una lista de mensajes")
        public void devuelveListaMensajes() {
            var peticion = get("http", "localhost", port, "/mensaje/centro?centro=1");

            var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<Mensaje>>() {});

            assertThat(respuesta.getStatusCodeValue()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

		@Test
		@DisplayName("error al obtener un mensaje concreto")
		public void errorAlObtenerMensajeConcreto() {
			var peticion = get("http", "localhost",port, "/mensaje/centro/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<MensajeDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}


        @Test
		@DisplayName("error al no encontrar el centro en el que se quiere buscar")
		public void errorAlBuscarCentro() {
			var peticion = get("http", "localhost", port, "/mensaje/centro?centro=999");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<MensajeDTO>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}

        @Test
		@DisplayName("devuelve error al eliminar un mensaje que no existe")
		public void eliminarMensajeInexistente() {
			var peticion = delete("http", "localhost",port, "/mensaje/centro1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

        @Nested
		@DisplayName("intenta insertar un mensaje")
		public class InsertaMensajes {
			@Test
			@DisplayName("y se guarda con éxito")
			public void sinID() {
				var mensaje = MensajeNuevoDTO.builder()
					.asunto("Asunto1")
                    .contenido("Contenido1")
					.build();
				var peticion = post("http", "localhost", port, "/mensaje/centro?centro=2", mensaje);
				
				var respuesta = restTemplate.exchange(peticion, Void.class);
				
				compruebaRespuesta(mensaje, respuesta);
			}

			private void compruebaRespuesta(MensajeNuevoDTO mensaje, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/mensaje");
				
				List<Mensaje> mensajes = mensajeRepo.findAll();
				assertThat(mensajes).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+ mensajes.get(0).getIdMensaje());
				compruebaCampos(mensaje, mensajes.get(0));
			}
		}
    }

    @Nested
	@DisplayName("cuando hay mensajes")
    public class HayMensajes {
		@BeforeEach
        public void setup() {
            var centro1 = Centro.builder().nombre("Centro 1").localizacion("A Coruña").build();
            var centro2 = Centro.builder().nombre("Centro 2").localizacion("A Coruña").build();
            var centro3 = Centro.builder().nombre("Centro 3").localizacion("A Coruña").build();
            centroRepository.saveAll(List.of(centro1, centro2, centro3));

        }

        private MensajeNuevoDTO mensaje1 = MensajeNuevoDTO.builder()
			.asunto("Asunto1")
            .contenido("Contenido1")
			.build();

		private MensajeNuevoDTO mensaje2 = MensajeNuevoDTO.builder()
			.asunto("Asunto2")
            .contenido("Contenido2")
			.build();


		@BeforeEach
		public void introduceDatos() {
			mensajeRepo.save(Mapper.toMensaje(mensaje1));
			mensajeRepo.save(Mapper.toMensaje(mensaje2));
		}

        @Test
        @DisplayName("obtener lista de mensajes de un centro correctamente")
        public void obtenerListaMensajesCentroCorrectamente() throws URISyntaxException {
            URI uri = new URI("http://localhost:" + port + "/mensaje/centro?centro=1");

            HttpHeaders headers = new HttpHeaders();

            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);

            ResponseEntity<List<Mensaje>> respuesta = restTemplate.exchange(
                    requestEntity,
                    new ParameterizedTypeReference<List<Mensaje>>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<Mensaje> listaMensajes = respuesta.getBody();
            assertThat(listaMensajes).isNotNull();
        }

        
        @Nested
		@DisplayName("intenta insertar un mensaje")
		public class InsertaMensajes {
			@Test
			@DisplayName("y se guarda con éxito")
			public void sinID() {
				var mensaje = MensajeNuevoDTO.builder()
					.asunto("Asunto1")
                    .contenido("Contenido1")
					.build();
				var peticion = post("http", "localhost", port, "/mensaje/centro?centro=1", mensaje);
				
				var respuesta = restTemplate.exchange(peticion, Void.class);
				
				compruebaRespuesta(mensaje, respuesta);
			}

			private void compruebaRespuesta(MensajeNuevoDTO mensaje, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/mensaje");
				
				List<Mensaje> mensajes = mensajeRepo.findAll();
				assertThat(mensajes).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+ mensajes.get(0).getIdMensaje());
				compruebaCampos(mensaje, mensajes.get(0));
			}
            @Test
            @DisplayName("y no encuentra el centro en el que se quiere añadir")
            public void errorAlBuscarCentro() {
                var peticion = get("http", "localhost", port, "/mensaje/centro?centro=999");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<MensajeDTO>() {});

                assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }
		}

        @Nested
		@DisplayName("al consultar un mensaje concreto")
		public class ObtenerMensajes {
			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveMensaje() {
				var peticion = get("http", "localhost",port, "/mensaje/centro/1");
				
				var respuesta = restTemplate.exchange(peticion, MensajeDTO.class);
				
				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.hasBody()).isTrue();
				assertThat(respuesta.getBody()).isNotNull();
			}
			
			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoMensajeNoExiste() {
				var peticion = get("http", "localhost",port, "/mensaje/centro/28");
				
				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<MensajeDTO>>() {});
				
				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
        
        @Nested
		@DisplayName("al eliminar un mensaje")
		public class EliminarMensajes {
			@Test
			@DisplayName("lo elimina cuando existe")
			public void eliminaCorrectamente() {
				var peticion = delete("http", "localhost",port, "/mensaje/centro/1");
				
				var respuesta = restTemplate.exchange(peticion,Void.class);
				
				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Mensaje> mensajes = mensajeRepo.findAll();
				assertThat(mensajes).hasSize(1);
				assertThat(mensajes).allMatch(c->c.getIdMensaje()!=1);
			}
			
			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExiste() {
				var peticion = delete("http", "localhost",port, "/mensaje/centro/28");
				
				var respuesta = restTemplate.exchange(peticion,Void.class);
				
				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
    }
}
