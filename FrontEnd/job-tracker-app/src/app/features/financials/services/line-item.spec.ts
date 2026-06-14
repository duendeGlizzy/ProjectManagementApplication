import { TestBed } from '@angular/core/testing';

import { LineItem } from './line-item';

describe('LineItem', () => {
  let service: LineItem;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LineItem);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
