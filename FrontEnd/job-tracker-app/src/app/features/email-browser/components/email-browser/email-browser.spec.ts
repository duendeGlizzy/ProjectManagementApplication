import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailBrowser } from './email-browser';

describe('EmailBrowser', () => {
  let component: EmailBrowser;
  let fixture: ComponentFixture<EmailBrowser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmailBrowser],
    }).compileComponents();

    fixture = TestBed.createComponent(EmailBrowser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
