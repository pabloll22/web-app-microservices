import { Component } from '@angular/core';
import { Usuario, UsuarioImpl } from '../entities/usuario';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Centro } from '../entities/centros';
import { CentroService } from '../services/centro.service';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-formulario-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-usuario.component.html',
  styleUrl: './formulario-usuario.component.css'
})
export class FormularioUsuarioComponent {
  accion?: "Añadir" | "Editar";
  _usuario: Usuario = new UsuarioImpl();
  rpassword: string = '';
  error: string = '';
  centros: Centro[] = []; // Definir la propiedad 'centros' para almacenar los centros existentes
  
  constructor(public modal: NgbActiveModal) { }

  get usuario () {
    return this._usuario;
  }

  set usuario(u: Usuario) {
    this._usuario = u;
    this._usuario.password='';
  }

  guardarUsuario(): void {
    if (this._usuario.password != this.rpassword) {
      this.error="Las contraseñas no coinciden";
      return;
    }

    this.modal.close(this.usuario);
  }

}
