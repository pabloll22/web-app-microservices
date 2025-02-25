import { Centro } from "./centros";

export interface Usuario {
  id: number;
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  password: string;
  administrador: boolean;
  gerente:boolean;
  centro?: Centro [];
}

export class UsuarioImpl implements Usuario {
  id: number;
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  password: string;
  administrador: boolean;
  gerente: boolean;
  centro?: Centro[];

  constructor() {
    this.id = 0;
    this.nombre = '';
    this.apellido1 = '';
    this.apellido2 = '';
    this.email = '';
    this.password = '';
    this.administrador = false;
    this.gerente = false;
    this.centro = [];
  }
}
