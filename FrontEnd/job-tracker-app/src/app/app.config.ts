import { ApplicationConfig, provideBrowserGlobalErrorListeners,provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";

import { routes } from './app.routes';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {cookieInterceptor} from './features/security/interceptors/cookie-interceptor';
import {errorInterceptor} from './features/security/interceptors/error-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(
      withFetch(),
      withInterceptors([
        cookieInterceptor,
        errorInterceptor
      ])
    ),
    provideAnimationsAsync()
  ]
};
