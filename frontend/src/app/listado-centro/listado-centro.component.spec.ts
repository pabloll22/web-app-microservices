import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoCentroComponent } from './listado-centro.component';

describe('ListadoCentroComponent', () => {
  let component: ListadoCentroComponent;
  let fixture: ComponentFixture<ListadoCentroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListadoCentroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListadoCentroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
