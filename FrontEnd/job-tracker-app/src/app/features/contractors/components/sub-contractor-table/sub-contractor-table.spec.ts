import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubContractorTable } from './sub-contractor-table';

describe('SubContractorTable', () => {
  let component: SubContractorTable;
  let fixture: ComponentFixture<SubContractorTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubContractorTable],
    }).compileComponents();

    fixture = TestBed.createComponent(SubContractorTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
