import { TestBed } from '@angular/core/testing';

import { PrimeContractor } from './prime-contractor';

describe('PrimeContractor', () => {
  let service: PrimeContractor;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrimeContractor);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
