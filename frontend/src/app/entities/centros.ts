export interface Centro{
    id: number;
    nombre: string;
    direccion: string;
    gerente: boolean;
}

export class CentroImpl implements Centro {
  id: number;
  nombre: string;
  direccion: string;
  gerente: boolean; // Propiedad para indicar si el centro tiene gerente

  constructor() {
    this.id = 0;
    this.nombre = '';
    this.direccion = '';
    this.gerente = false;
  }
}
  