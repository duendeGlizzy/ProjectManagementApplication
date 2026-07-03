import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceRequest } from './invoice-request';

describe('InvoiceRequest', () => {
  let component: InvoiceRequest;
  let fixture: ComponentFixture<InvoiceRequest>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceRequest],
    }).compileComponents();

    fixture = TestBed.createComponent(InvoiceRequest);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
