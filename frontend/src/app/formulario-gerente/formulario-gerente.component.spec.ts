import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioGerenteComponent } from './formulario-gerente.component';

describe('FormularioGerenteComponent', () => {
  let component: FormularioGerenteComponent;
  let fixture: ComponentFixture<FormularioGerenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioGerenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
