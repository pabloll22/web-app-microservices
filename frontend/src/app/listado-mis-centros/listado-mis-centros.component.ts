import { Component } from '@angular/core';
import { Centro } from '../entities/centros';
import { Rol } from '../entities/login';
import { UsuariosService } from '../services/usuarios.service';
import { CommonModule } from '@angular/common';
import { Usuario } from '../entities/usuario';
import { Router } from '@angular/router';
import { CentroService } from '../services/centro.service';

@Component({
  selector: 'app-listado-mis-centros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listado-mis-centros.component.html',
  styleUrl: './listado-mis-centros.component.css'
})

export class ListadoMisCentrosComponent {
  centros: Centro [] = [];
  usuarios: Usuario [] = [];


  constructor(private usuarioService: UsuariosService, private router: Router, private centroService: CentroService) {
   }

  private get rol() {
    return this.usuarioService.rolCentro;
  }

  ngOnInit(): void {
    this.actualizarUsuarios();
    this.actualizarCentros();
  }

  actualizarCentros() {
    const u = this.usuarioService.getUsuarioSesion();
    if(u){
      // busco en usuarios el usuario actual y si tiene centros los añado a la lista de centros
      this.usuarios.find(usuario => usuario.id == u.id)?.centro?.forEach(centro => {
        this.centros.push(centro);
      }
    )};
  }

  isGerente(): boolean {
    console.log("Pregunta gerente: "+this.rol);
    return this.rol?.rol == Rol.GERENTE;
  }

  actualizarUsuarios() {
    this.usuarioService.getUsuarios().subscribe(usuarios => {
      this.usuarios = usuarios;
    });
  }  

  paginaMensajes(idCentro: number) {
    this.router.navigate(['/mensajes'], { queryParams: { idCentro: idCentro } });
  }
}

