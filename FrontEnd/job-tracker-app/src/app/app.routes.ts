import { Routes } from '@angular/router';
import { JobForm } from './features/jobs/components/job-form/job-form'
import {JobDashboard} from './features/jobs/components/job-dashboard/job-dashboard';
import {JobDetails} from './features/jobs/components/job-details/job-details';
import {TaskDetails} from './features/jobs/components/task-details/task-details';
import {TaskForm} from './features/jobs/components/task-form/task-form';
import {ContractorDashboard} from './features/contractors/components/contractor-dashboard/contractor-dashboard';
import {SubContractorForm} from './features/contractors/components/sub-contractor-form/sub-contractor-form';
import {PrimeContractorForm} from './features/contractors/components/prime-contractor-form/prime-contractor-form';
import {VendorForm} from './features/contractors/components/vendor-form/vendor-form';
import {ClientDashboard} from './features/jobs/components/client-dashboard/client-dashboard';
import {ClientForm} from './features/jobs/components/client-form/client-form';
import {BillForm} from './features/financials/components/bill-form/bill-form';
import {PaymentForm} from './features/financials/components/payment-form/payment-form';
import {FinancialDashboard} from './features/financials/components/financial-dashboard/financial-dashboard';
import {Login} from './features/security/components/login/login';
import {authGuard} from './features/security/guards/auth-guard';
import {Homepage} from './features/public/component/homepage/homepage';
import {InvoiceRequest} from './features/public/component/invoice-request/invoice-request';
import { EmailBrowser } from './features/email-browser/components/email-browser/email-browser';
import {FileBrowser} from './features/file-browser/components/file-browser/file-browser';
import {EmployeeDetails} from './features/employee/components/employee-details/employee-details';
import { LoginAdmin } from './features/security/components/login-admin/login-admin';
import {AdminDashboard} from './features/admin/components/admin-dashboard/admin-dashboard';



export const routes: Routes = [
  { path: '', redirectTo: 'homepage', pathMatch: 'full' },

  {path: 'homepage', component:Homepage},
  {path: 'request-invoice', component:InvoiceRequest},

  {path: 'login', component: Login},
  {path: 'login/admin', component: LoginAdmin},


  { path: 'jobs', component: JobDashboard, canActivate: [authGuard] },

  { path: 'jobs/new', component: JobForm, canActivate: [authGuard] },
  { path: 'jobs/new/:id', component: JobForm, canActivate: [authGuard] },
  { path: 'jobs/:id', component: JobDetails, canActivate: [authGuard] },

  {path: 'jobs/:id/bills/new', component: BillForm, canActivate: [authGuard] },
  {path: 'jobs/:id/payments/new', component: PaymentForm, canActivate: [authGuard] },


  { path: 'tasks/new', component: TaskForm, canActivate: [authGuard] },
  { path: 'tasks/new/:id', component: TaskForm, canActivate: [authGuard] },
  { path: 'tasks/:id', component: TaskDetails, canActivate: [authGuard] },

  {path: 'contractors', component: ContractorDashboard, canActivate: [authGuard] },

  {path: 'contractors/subContractor/new', component: SubContractorForm, canActivate: [authGuard] },
  {path: 'contractors/subContractor/new/:id', component: SubContractorForm, canActivate: [authGuard] },

  {path: 'contractors/primeContractor/new', component: PrimeContractorForm, canActivate: [authGuard] },
  {path: 'contractors/primeContractor/new/:id', component: PrimeContractorForm, canActivate: [authGuard] },

  {path: 'contractors/vendor/new', component: VendorForm, canActivate: [authGuard] },
  {path: 'contractors/vendor/new/:id', component: VendorForm, canActivate: [authGuard] },

  {path: 'clients', component: ClientDashboard, canActivate: [authGuard] },
  {path: 'clients/new', component: ClientForm, canActivate: [authGuard] },
  {path: 'clients/new/:id', component: ClientForm, canActivate: [authGuard] },

  {path: 'financials', component: FinancialDashboard, canActivate: [authGuard] },

  {path: 'email', component:EmailBrowser, canActivate: [authGuard] },

  {path: 'file-browser', component: FileBrowser, canActivate: [authGuard] },

  {path: 'employee-details', component: EmployeeDetails, canActivate: [authGuard] },

  {path: 'admin', component: AdminDashboard, canActivate: [authGuard] },



  { path: '**', redirectTo: 'homepage' }


];
