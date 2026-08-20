import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PcPartList } from './pc-part-list';

describe('PcPartList', () => {
  let component: PcPartList;
  let fixture: ComponentFixture<PcPartList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PcPartList],
    }).compileComponents();

    fixture = TestBed.createComponent(PcPartList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
