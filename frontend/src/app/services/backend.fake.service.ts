import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { SECRET_JWT } from "../config/config";
import { from } from "rxjs";
import * as jose from "jose";
import { FRONTEND_URI } from "../config/config";
import { Centro } from "../entities/centros";
import { Mensaje } from "../entities/mensajes";

// Este servicio imita al backend pero utiliza localStorage para almacenar los datos
const mensajesC: Mensaje[] = [
  {
    idCentro: 1,
    id: 1,
    emisor: 'usuario1@example.com',
    receptor: 'usuario2@example.com',
    asunto: 'Reunión',
    cuerpo: 'Hagamos una reunión para discutir los próximos pasos del proyecto.'
  },
  {
    idCentro: 2,
    id: 2,
    emisor: 'usuario3@example.com',
    receptor: 'usuario1@example.com',
    asunto: 'Solicitud de vacaciones',
    cuerpo: 'Quisiera solicitar vacaciones del 15 al 30 de abril.'
  },
  {
    idCentro: 1,
    id: 3,
    emisor: 'usuario2@example.com',
    receptor: 'usuario3@example.com',
    asunto: 'Actualización del informe',
    cuerpo: 'Te adjunto la última versión del informe para su revisión.'
  }
];

const centrosC: Centro [] = [
  {
    id: 1,
    nombre: 'Vals',
    direccion: 'C/Oporto, 39',
    gerente: false
  },
  {
    id: 2,
    nombre: 'Cónsul',
    direccion: 'C/Nápoles, 10',
    gerente: false
  },
];

const usuariosC: Usuario [] = [
  {
    id: 1,
    nombre: 'Admin',
    apellido1: 'Admin',
    apellido2: 'Admin',
    email: 'admin@uma.es',
    administrador: true,
    gerente: false,
    password: 'admin',
    centro: []
  },
  {
    id: 2,
    nombre: 'Antonio',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'antonio@uma.es',
    administrador: false,
    gerente: false,
    password: '5678',
    centro: []
  },
];

@Injectable({
  providedIn: 'root'
})
export class BackendFakeService {
  private usuarios: Usuario [];
  private centros: Centro [];
  private mensajes: Mensaje [];
  private forgottenPasswordTokens;

  constructor() {
    let _usuarios = localStorage.getItem('usuarios');
    if (_usuarios) {
      this.usuarios = JSON.parse(_usuarios);
    } else {
      this.usuarios = [...usuariosC];
    }

    let _centros = localStorage.getItem('centros');
    if (_centros) {
      this.centros = JSON.parse(_centros);
    } else {
      this.centros = [...centrosC];
    } 

    let _mensajes = localStorage.getItem('mensajes');
    if (_mensajes) {
      this.mensajes = JSON.parse(_mensajes);
    } else {
      this.mensajes = [...mensajesC];
      this.guardarMensajeEnLocalStorage();
    } 

    let _forgottenPasswordTokens = localStorage.getItem('forgottenPasswordTokens');
    if (_forgottenPasswordTokens) {
      this.forgottenPasswordTokens = new Map(JSON.parse(_forgottenPasswordTokens));
    } else {
      this.forgottenPasswordTokens = new Map();
    }
  }

  getUsuarios(): Observable<Usuario[]> {
    return of(this.usuarios);
  }

  postUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.email == usuario.email);
    if (!usuario.email) {
      return new Observable<Usuario>(observer => {
        observer.error('El email es obligatorio');
      });
    }
    if (u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario ya existe');
      });
    }
    // Si no trae contraseña generamos una aleatoria
    if (usuario.password.length == 0) {
      usuario.password = this.generarCadena();
    }

    usuario.id = this.usuarios.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    this.usuarios.push(usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(usuario);
  }

  private guardarUsuariosEnLocalStorage() {
    localStorage.setItem('usuarios', JSON.stringify(this.usuarios));
  }

  private guardarForgottenPasswordTokensEnLocalStorage() {
    localStorage.setItem('forgottenPasswordTokens', JSON.stringify(Array.from(this.forgottenPasswordTokens.entries())));
  }

  putUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == usuario.id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    // Si la contraseña está en blanco mantenemos la que ya tiene
    if (usuario.password.length == 0) {
      usuario.password = u.password;
    }

    Object.assign(u, usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(u);
  }

  deleteUsuario(id: number): Observable<void> {
    let i = this.usuarios.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('El usuario no existe');
      });
    }
    this.usuarios.splice(i, 1);
    this.guardarUsuariosEnLocalStorage();
    return of();
  }

  deleteGerente(id: number): Observable<void> {
    const usuario = this.usuarios.find(u => u.id === id);
  
    if (usuario && usuario.gerente) {
      usuario.gerente = false; // Cambiar la propiedad gerente a false

      // Verificar que usuario.centro esté definido antes de manipularlo
      if (usuario.centro) {
        // Desvincular usuario de sus centros
        usuario.centro.forEach(centro => centro.gerente = false);
        usuario.centro = [];
      }

      this.guardarUsuariosEnLocalStorage(); // Guardar los cambios en el almacenamiento local si es necesario
      return of(); // Devolver un observable de éxito
    } else {
      // Si el usuario no existe o no es un gerente, devolver un observable de error
      return new Observable<void>(observer => {
        observer.error('El usuario no es un gerente o no existe');
      });
    }
  }


  


  getUsuario(id: number): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    return of(u);
  }

  login(email: string, password: string): Observable<string> {
    let u = this.usuarios.find(u => u.email == email && u.password == password);
    if (!u) {
      return new Observable<string>(observer => {
        observer.error({status: 401, statusText: 'Usuario o contraseña incorrectos'});
      });
    }
    return from(this.generateJwt(u));
  }

  forgottenPassword(email: string): Observable<void> {
    const token = this.generarCadena()
    console.log('Para resetar la contraseña acceda a: '+FRONTEND_URI+'/reset-password?token='+token);
    this.forgottenPasswordTokens.set(token, email);
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  resetPassword(token: string, password: string): Observable<void> {
    if (!this.forgottenPasswordTokens.get(token)) {
      return new Observable<void>(observer => {
        observer.error('Token incorrecto');
      });
    }
    let email = this.forgottenPasswordTokens.get(token);
    console.log("Email for token: ", email)
    let u = this.usuarios.find(u => u.email == email);
    if (!u) {
      return new Observable<void>(observer => {
        observer.error('Usuario no existe');
      });
    }
    u.password = password;
    this.forgottenPasswordTokens.delete(token);

    this.guardarUsuariosEnLocalStorage();
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  private generateJwt(usuario: Usuario): Promise<string> {
    const secret = new TextEncoder().encode(SECRET_JWT);
    return new jose.SignJWT({ sub: ""+usuario.id, email: usuario.email })
      .setProtectedHeader({ alg: 'HS256' })
      .sign(secret);
  }

  private generarCadena(): string {
    return Math.random().toString(36).substring(2);
  }

  private guardarCentroEnLocalStorage() {
    localStorage.setItem('centros', JSON.stringify(this.centros));
  }


  getCentros(): Observable<Centro[]> {
    return of(this.centros);
  }

  // Función para agregar un nuevo centro
  postCentro(centro: Centro): Observable<Centro> {

    // Si no hay centros, agregar el nuevo centro
    if (this.centros.length === 0) {
      centro.id = 1; // Establecer el ID del primer centro como 1
      this.centros.push(centro);
      this.guardarCentroEnLocalStorage();
      return of(centro);
    }

    let u = this.centros.find(u => u.nombre == centro.nombre);
    if (!centro.nombre) {
      return new Observable<Centro>(observer => {
        observer.error('El nombre es obligatorio');
      });
    }
    if (u) {
      return new Observable<Centro>(observer => {
        observer.error('El centro ya existe');
      });
    }

    centro.id = this.centros.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    this.centros.push(centro);
    this.guardarCentroEnLocalStorage();
    return of(centro);
  }

  // Función para actualizar un centro existente
  putCentro(centro: Centro): Observable<Centro> {
    let u = this.centros.find(u => u.id == centro.id);
    if (!u) {
      return new Observable<Centro>(observer => {
        observer.error('El centro no existe');
      });
    }

    Object.assign(u, centro);
    this.guardarCentroEnLocalStorage();
    return of(u);
  }

// Función para eliminar un centro por su id
  deleteCentro(id: number): Observable<void> {
    let i = this.centros.findIndex(u => u.id == id);
    if (i < 0) {
        return new Observable<void>(observer => {
            observer.error('El centro no existe');
        });
    }

    // Eliminar el centro del array de centros de todos los usuarios que lo tengan asignado
    this.usuarios.forEach(usuario => {
        if (usuario.centro) {
            const index = usuario.centro.findIndex(centro => centro.id === id);
            if (index !== -1) {
                usuario.centro.splice(index, 1);
            }
        }
    });

    this.centros.splice(i, 1);
    this.guardarCentroEnLocalStorage();
    this.guardarUsuariosEnLocalStorage(); // Guardar los cambios en los usuarios
    return of();
  }

  agregarCentroGerente(centro: Centro, usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id === usuario.id);
    if (!u) {
        return new Observable<Usuario>(observer => {
            observer.error('El usuario no existe');
        });
    }

    // Verificar si el usuario tiene la propiedad "centro" definida
    if (!u.centro) {
        u.centro = []; // Si no tiene, inicializa el array
    }

    // Agregar el nuevo centro al array de centros del usuario
    u.centro.push(centro);

    // Actualizar la propiedad gerente del centro a true
    const centroUsuario = this.centros.find(c => c.id === centro.id);
    if (centroUsuario) {
        centroUsuario.gerente = true;
    }

    // Guardar los cambios en el almacenamiento local
    this.guardarUsuariosEnLocalStorage();
    this.guardarCentroEnLocalStorage();

    return of(u);
  }


  desagregarCentroGerente(centro: Centro, usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id === usuario.id);
    if (!u) {
        return new Observable<Usuario>(observer => {
            observer.error('El usuario no existe');
        });
    }

    // Quitar el centro del array de centros del usuario si está presente
    if (u.centro) {
        u.centro = u.centro.filter(c => c.id !== centro.id);
    }

    // Actualizar la propiedad gerente del centro a false
    const centroUsuario = this.centros.find(c => c.id === centro.id);
    if (centroUsuario) {
        centroUsuario.gerente = false;
    }

    // Guardar los cambios en el almacenamiento local
    this.guardarUsuariosEnLocalStorage();
    this.guardarCentroEnLocalStorage();

    return of(u);
  }

  private guardarMensajeEnLocalStorage() {
    localStorage.setItem('mensajes', JSON.stringify(this.mensajes));
  }


  getMensajes(): Observable<Mensaje[]> {
    return of(this.mensajes);
  }

  getMensajesCentro(idCentro: number): Observable<Mensaje[]> {
    const mensajesCentro = this.mensajes.filter(mensaje => mensaje.idCentro === idCentro);
    return of(mensajesCentro);
  }

  deleteMensaje(id: number): Observable<void> {
    const index = this.mensajes.findIndex(mensaje => mensaje.id === id);
    if (index === -1) {
        return new Observable<void>(observer => {
            observer.error('El mensaje no existe');
        });
    }

    this.mensajes.splice(index, 1); // Eliminar el mensaje del arreglo de mensajes
    this.guardarMensajeEnLocalStorage(); // Guardar los cambios en el almacenamiento local

    return of(); // Devolver un observable de éxito
  }

}
