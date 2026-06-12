import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractorDashboard } from './contractor-dashboard';

describe('ContractorDashboard', () => {
  let component: ContractorDashboard;
  let fixture: ComponentFixture<ContractorDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractorDashboard],
    }).compileComponents();

    fixture = TestBed.createComponent(ContractorDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
