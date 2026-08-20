import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PcPartForm } from './pc-part-form';

describe('PcPartForm', () => {
  let component: PcPartForm;
  let fixture: ComponentFixture<PcPartForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PcPartForm],
    }).compileComponents();

    fixture = TestBed.createComponent(PcPartForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
