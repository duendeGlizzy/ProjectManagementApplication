import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrimeContractorForm } from './prime-contractor-form';

describe('PrimeContractorForm', () => {
  let component: PrimeContractorForm;
  let fixture: ComponentFixture<PrimeContractorForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrimeContractorForm],
    }).compileComponents();

    fixture = TestBed.createComponent(PrimeContractorForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
