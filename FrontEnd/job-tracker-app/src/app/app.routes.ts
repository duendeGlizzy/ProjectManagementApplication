import { Routes } from '@angular/router';
import { JobForm } from './features/jobs/components/job-form/job-form'
import {JobDashboard} from './features/jobs/components/job-dashboard/job-dashboard';
import {JobDetails} from './features/jobs/components/job-details/job-details';
import {TaskDetails} from './features/jobs/components/task-details/task-details';
import {TaskForm} from './features/jobs/components/task-form/task-form';
import {ContractorDashboard} from './features/contractors/components/contractor-dashboard/contractor-dashboard';
import {FinancialDashboard} from './features/financials/components/financial-dashboard/financial-dashboard';
import {SubContractorForm} from './features/contractors/components/sub-contractor-form/sub-contractor-form';
import {PrimeContractorForm} from './features/contractors/components/prime-contractor-form/prime-contractor-form';
import {VendorForm} from './features/contractors/components/vendor-form/vendor-form';
import {ClientDashboard} from './features/jobs/components/client-dashboard/client-dashboard';
import {ClientForm} from './features/jobs/components/client-form/client-form';



export const routes: Routes = [
  { path: '', redirectTo: 'jobs', pathMatch: 'full' },


  { path: 'jobs', component: JobDashboard },

  { path: 'jobs/new', component: JobForm },
  { path: 'jobs/new/:id', component: JobForm },
  { path: 'jobs/:id', component: JobDetails },

  { path: 'tasks/new', component: TaskForm },
  { path: 'tasks/new/:id', component: TaskForm },
  { path: 'tasks/:id', component: TaskDetails },

  {path: 'contractors', component: ContractorDashboard},

  {path: 'contractors/subContractor/new', component: SubContractorForm },
  {path: 'contractors/subContractor/new/:id', component: SubContractorForm},

  {path: 'contractors/primeContractor/new', component: PrimeContractorForm},
  {path: 'contractors/primeContractor/new/:id', component: PrimeContractorForm},

  {path: 'contractors/vendor/new', component: VendorForm},
  {path: 'contractors/vendor/new/:id', component: VendorForm},

  {path: 'clients', component: ClientDashboard},
  {path: 'clients/new', component: ClientForm},
  {path: 'clients/new/:id', component: ClientForm},

  {path: 'financials', component: FinancialDashboard},





  { path: '**', redirectTo: 'jobs' }


];
