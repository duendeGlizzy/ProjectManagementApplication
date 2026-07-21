import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceArea } from './service-area';

describe('ServiceArea', () => {
  let component: ServiceArea;
  let fixture: ComponentFixture<ServiceArea>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceArea],
    }).compileComponents();

    fixture = TestBed.createComponent(ServiceArea);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
