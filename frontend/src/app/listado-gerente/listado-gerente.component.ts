import { Component } from '@angular/core';
import { Usuario, UsuarioImpl } from '../entities/usuario';
import { UsuariosService } from '../services/usuarios.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Rol } from '../entities/login';
import { CommonModule } from '@angular/common';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';

@Component({
  selector: 'app-listado-gerente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listado-gerente.component.html',
  styleUrl: './listado-gerente.component.css'
})
export class ListadoGerenteComponent {
  usuarios: Usuario [] = [];

  constructor(private usuariosService: UsuariosService, private modalService: NgbModal) {
    this.actualizarUsuarios();
   }

  private get rol() {
    return this.usuariosService.rolCentro;
  }

  isAdministrador(): boolean {
    console.log("Pregunta admin: "+this.rol);
    return this.rol?.rol == Rol.ADMINISTRADOR;
  }

  ngOnInit(): void {
    this.actualizarUsuarios();
  }

  actualizarUsuarios() {
    this.usuariosService.getUsuarios().subscribe(usuarios => {
      this.usuarios = usuarios;
    });
  }

  private usuarioEditado(usuario: Usuario): void {
    this.usuariosService.editarUsuario(usuario).subscribe(() => {
      this.actualizarUsuarios();
    });
  }

  eliminarGerente(id: number): void {
    this.usuariosService.eliminarGerente(id).subscribe(() => {
      this.actualizarUsuarios();
    });
  }

  editarUsuario(usuario: Usuario): void {
    let ref = this.modalService.open(FormularioGerenteComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.usuario = {...usuario};
    ref.result.then((usuario: Usuario) => {
      this.usuarioEditado(usuario);
    }, (reason) => {});
  }
}
