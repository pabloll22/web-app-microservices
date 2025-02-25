import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Centro, CentroImpl } from '../entities/centros';

@Component({
  selector: 'app-formulario-centro',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-centro.component.html',
  styleUrl: './formulario-centro.component.css'
})
export class FormularioCentroComponent {
  accion?: "Añadir" | "Editar";
  _centro: Centro = new CentroImpl();

  constructor(public modal: NgbActiveModal) { }

  get centro () {
    return this._centro;
  }

  set centro(u: Centro) {
    this._centro = u;
  }

  guardarCentro(): void {
    this.modal.close(this.centro);
  }
}
  
