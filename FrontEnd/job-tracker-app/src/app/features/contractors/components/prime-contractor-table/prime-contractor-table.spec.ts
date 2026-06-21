import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrimeContractorTable } from './prime-contractor-table';

describe('PrimeContractorTable', () => {
  let component: PrimeContractorTable;
  let fixture: ComponentFixture<PrimeContractorTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrimeContractorTable],
    }).compileComponents();

    fixture = TestBed.createComponent(PrimeContractorTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
