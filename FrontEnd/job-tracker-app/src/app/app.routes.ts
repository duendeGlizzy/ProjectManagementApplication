import { Routes } from '@angular/router';
import { JobForm } from './features/jobs/components/job-form/job-form'
import {JobDashboard} from './features/jobs/components/job-dashboard/job-dashboard';
import {JobDetails} from './features/jobs/components/job-details/job-details';



export const routes: Routes = [
  { path: '', redirectTo: 'jobs', pathMatch: 'full' },

  // 2. CORE VIEW TARGET PATHS
  { path: 'jobs', component: JobDashboard },
  { path: 'jobs/new', component: JobForm },
  { path: 'jobs/new/:id', component: JobForm },
  { path: 'jobs/:id', component: JobDetails },

  // 3. FALLBACK WILDCARD: Handles typos (Must stay at the very bottom!)
  { path: '**', redirectTo: 'jobs' }


];
