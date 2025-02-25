import { Component } from '@angular/core';
import { Usuario, UsuarioImpl } from '../entities/usuario';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Centro } from '../entities/centros';
import { CentroService } from '../services/centro.service';
import { UsuariosService } from '../services/usuarios.service';
import { catchError, finalize, tap } from 'rxjs';

@Component({
  selector: 'app-formulario-gerente',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-gerente.component.html',
  styleUrl: './formulario-gerente.component.css'
})
export class FormularioGerenteComponent {
  accion?: "Añadir" | "Editar";
  _usuario: Usuario = new UsuarioImpl();
  error: string = '';
  centros: Centro[] = []; // Definir la propiedad 'centros' para almacenar los centros existentes
  
  constructor(public modal: NgbActiveModal, private centroService: CentroService, private usuarioService: UsuariosService) { }

  ngOnInit(): void {
    this.loadCentros(); // Cargar los centros existentes al iniciar el componente
  }

  loadCentros(): void {
    this.centroService.getCentros().subscribe(
      centros => {
        // Filtrar los centros que tienen gerente igual a false
        this.centros = centros.filter(centro => !centro.gerente);
      },
      error => {
        console.error('Error al cargar los centros:', error);
      }
    );
  }
  

  get usuario () {
    return this._usuario;
  }

  set usuario(u: Usuario) {
    this._usuario = u;
  }

  asignarCentroGerente(centro: Centro): void {
    this.usuarioService.agregarCentroGerente(centro, this.usuario).subscribe(
      usuario => {
        // Actualizar el usuario con los centros asignados
        this.usuario = usuario;
      },
      error => {
        console.error('Error al asignar centro al usuario:', error);
      }
    );
    this.loadCentros();
  }

  desasignarCentroGerente(centro: Centro): void {
    this.usuarioService.desagregarCentroGerente(centro, this.usuario).subscribe(
      usuario => {
        // Actualizar el usuario con los centros asignados
        this.usuario = usuario;
      },
      error => {
        console.error('Error al desasignar centro al usuario:', error);
      }
    );
    this.loadCentros();
  }

  guardarUsuario(): void {
    this.modal.close(this.usuario);
  }
}
