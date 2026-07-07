import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LicenceTable } from './licence-table';

describe('LicenceTable', () => {
  let component: LicenceTable;
  let fixture: ComponentFixture<LicenceTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LicenceTable],
    }).compileComponents();

    fixture = TestBed.createComponent(LicenceTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
