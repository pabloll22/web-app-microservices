export interface Mensaje{
    idCentro: number;
    id: number;
    emisor: string;
    receptor: string;
    asunto: string;
    cuerpo: string;
}

export class MensajeImpl implements Mensaje {
  idCentro: number;
  id: number;
  emisor: string;
  receptor: string;
  asunto: string;
  cuerpo: string;

  constructor() {
    this.idCentro = 0;
    this.id = 0;
    this.emisor = '';
    this.receptor = '';
    this.asunto = '';
    this.cuerpo = '';
  }
}