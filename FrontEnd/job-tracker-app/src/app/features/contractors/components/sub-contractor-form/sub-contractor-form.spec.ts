import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubContractorForm } from './sub-contractor-form';

describe('SubContractorForm', () => {
  let component: SubContractorForm;
  let fixture: ComponentFixture<SubContractorForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubContractorForm],
    }).compileComponents();

    fixture = TestBed.createComponent(SubContractorForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
