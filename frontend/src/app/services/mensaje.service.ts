import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import * as jose from 'jose';

import { BackendFakeService } from "./backend.fake.service";
import { BackendService } from "./backend.service";
import { Centro } from "../entities/centros";
import { Mensaje } from "../entities/mensajes";

@Injectable({
  providedIn: 'root'
})
export class MensajeService {

  constructor(private backend: BackendFakeService) {}

  private getMensajeIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: "+JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  getMensajes(): Observable<Mensaje[]> {
    return this.backend.getMensajes();
  }

  getMensajesCentro(idCentro: number): Observable<Mensaje[]> {
    return this.backend.getMensajesCentro(idCentro);
  }

  deleteMensaje(id: number): Observable<void> {
    return this.backend.deleteMensaje(id);
  }


}

