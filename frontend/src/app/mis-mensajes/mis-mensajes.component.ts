import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensajes';
import { MensajeService } from '../services/mensaje.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mis-mensajes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-mensajes.component.html',
  styleUrl: './mis-mensajes.component.css'
})
export class MisMensajesComponent {

  mensajes: Mensaje [] = [];
  idCentro: number = 0;

  constructor(private mensajeService: MensajeService, private route: ActivatedRoute, private router: Router){}

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
      this.idCentro = params ['idCentro'];
      this.actualizarMensajes();
    })
  }

  actualizarMensajes(){
    this.mensajeService.getMensajes().subscribe(mensajes => {
      this.mensajes = mensajes.filter(mensaje => mensaje.idCentro == this.idCentro)
    })
  }

  volverAtras(){
    this.router.navigate(['/mis_centros']);
  }
}
