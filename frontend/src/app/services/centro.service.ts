import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import * as jose from 'jose';

import { BackendFakeService } from "./backend.fake.service";
import { BackendService } from "./backend.service";
import { Centro } from "../entities/centros";

@Injectable({
  providedIn: 'root'
})
export class CentroService {

  constructor(private backend: BackendFakeService) {}

  private getCentroIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: "+JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  

  getCentros(): Observable<Centro[]> {
    return this.backend.getCentros();
  }

  editarCentro(centro: Centro): Observable<Centro> {
    return this.backend.putCentro(centro);
  }

  eliminarCentro(id: number): Observable<void> {
    return this.backend.deleteCentro(id);
  }

  aniadirCentro(centro: Centro): Observable<Centro> {
    return this.backend.postCentro(centro);
  }


}

