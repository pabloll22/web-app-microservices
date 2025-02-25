import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoGerenteComponent } from './listado-gerente.component';

describe('ListadoGerenteComponent', () => {
  let component: ListadoGerenteComponent;
  let fixture: ComponentFixture<ListadoGerenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoGerenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListadoGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
