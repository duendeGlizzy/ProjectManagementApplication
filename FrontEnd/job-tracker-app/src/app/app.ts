import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {JobDashboard} from './features/jobs/components/job-dashboard/job-dashboard';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JobDashboard],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('job-tracker-app');
}
