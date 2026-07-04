import { TestBed } from '@angular/core/testing';

import { EmailBrowser } from './email-browser';

describe('EmailBrowser', () => {
  let service: EmailBrowser;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailBrowser);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
