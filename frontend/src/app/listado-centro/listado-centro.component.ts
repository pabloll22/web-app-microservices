import { Component } from '@angular/core';
import { Centro, CentroImpl } from '../entities/centros';
import { Rol } from '../entities/login';
import { CentroService } from '../services/centro.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UsuariosService } from '../services/usuarios.service';
import { CommonModule } from '@angular/common';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';

@Component({
  selector: 'app-listado-centro',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listado-centro.component.html',
  styleUrl: './listado-centro.component.css'
})

export class ListadoCentroComponent {
  centros: Centro [] = [];

  constructor(private usuarioService: UsuariosService, private centroService: CentroService, private modalService: NgbModal) {
    this.actualizarCentro();
   }

  private get rol() {
    return this.usuarioService.rolCentro;
  }

  isAdministrador(): boolean {
    console.log("Pregunta admin: "+this.rol);
    return this.rol?.rol == Rol.ADMINISTRADOR;
  }

  ngOnInit(): void {
    this.actualizarCentro();
  }

  actualizarCentro() {
    this.centroService.getCentros().subscribe(centros => {
      this.centros = centros;
    });
  }

  private centroEditado(centro: Centro): void {
    this.centroService.editarCentro(centro).subscribe(() => {
      this.actualizarCentro();
    });
  }

  eliminarCentro(id: number): void {
    this.centroService.eliminarCentro(id).subscribe(() => {
      this.actualizarCentro();
    });
  }
  
  editarCentro(centro: Centro): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.centro = {...centro};
    ref.result.then((centro: Centro) => {
      this.centroEditado(centro);
    }, (reason) => {}); 
  }

  aniadirCentro(): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.centro = new CentroImpl();
    ref.result.then((centro: Centro) => {
      this.centroService.aniadirCentro(centro).subscribe(centro => {
        this.actualizarCentro();
      });
    }, (reason) => {});

  }
  
}
