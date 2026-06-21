import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancialDashboard } from './financial-dashboard';

describe('FinancialDashboard', () => {
  let component: FinancialDashboard;
  let fixture: ComponentFixture<FinancialDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinancialDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(FinancialDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
