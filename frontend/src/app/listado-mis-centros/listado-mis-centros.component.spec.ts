import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoMisCentrosComponent } from './listado-mis-centros.component';

describe('ListadoMisCentrosComponent', () => {
  let component: ListadoMisCentrosComponent;
  let fixture: ComponentFixture<ListadoMisCentrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoMisCentrosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListadoMisCentrosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
