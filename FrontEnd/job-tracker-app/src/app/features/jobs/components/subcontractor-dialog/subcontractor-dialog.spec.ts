import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubcontractorDialog } from './subcontractor-dialog';

describe('SubcontractorDialog', () => {
  let component: SubcontractorDialog;
  let fixture: ComponentFixture<SubcontractorDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubcontractorDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(SubcontractorDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
