import { TestBed } from '@angular/core/testing';

import { SubContractor } from './sub-contractor';

describe('SubContractor', () => {
  let service: SubContractor;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubContractor);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
