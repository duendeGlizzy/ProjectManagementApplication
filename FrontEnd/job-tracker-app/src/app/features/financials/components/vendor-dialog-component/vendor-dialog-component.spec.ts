import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorDialogComponent } from './vendor-dialog-component';

describe('VendorDialogComponent', () => {
  let component: VendorDialogComponent;
  let fixture: ComponentFixture<VendorDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendorDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(VendorDialogComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
