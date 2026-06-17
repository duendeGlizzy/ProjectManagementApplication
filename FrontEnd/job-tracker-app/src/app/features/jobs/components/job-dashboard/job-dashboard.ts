import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobService } from '../../services/job';
import { Job } from '../../models/job.model'
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import {RouterLink} from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule,FormControl,ReactiveFormsModule } from '@angular/forms';
import {JobStatus} from '../../models/job-status.enum';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import {map, Observable, startWith} from 'rxjs';
import { AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-job-dashboard',
  standalone: true,
  imports: [CommonModule, MatChipsModule,
    MatTableModule, MatButtonModule, RouterLink,
    FormsModule, MatFormFieldModule, MatInputModule,
  MatInputModule, MatSelectModule, MatIconModule,
  ReactiveFormsModule,MatAutocompleteModule],
  templateUrl: './job-dashboard.html',
  styleUrl: './job-dashboard.css',
})
export class JobDashboard implements OnInit {
  jobsList = signal<Job[]>([]);
  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  //autocomplete datasets
  uniqueClients: string[] = [];
  uniqueCompanies: string[] = [];

  //filter Steams
  filteredClients!: Observable<string[]>;
  filteredCompanies!: Observable<string[]>;

  clientCtrl = new FormControl('', [this.autocompleteValidator(() => this.uniqueClients)]);
  companyCtrl = new FormControl('', [this.autocompleteValidator(() => this.uniqueCompanies)]);
  statusFilter: JobStatus | '' = '';


  searchFilters = {
    lastName: '',
    companyName: '',
    status: '' as JobStatus |''
  };

  statusOptions = [
    { value: 'NOT_STARTED', label: 'Not Started'},
    {value: 'STARTED', label: 'Started'},
    {value: 'COMPLETED', label: 'Completed'},
  ];


  displayedColumns: string[] = [
    'jobId',
    'description',
    'jobType',
    'estimatedCost',
    'totalPayment',
    'client',
    'primeContractor',
    'status'
  ];

  constructor(private jobService: JobService) { }

  ngOnInit() {
    this.loadAllJobs();
  }

  loadAllJobs() {
    this.jobService.getAllJobs().subscribe({
      next: (data: Job[]) => {
        this.jobsList.set(data);
        this.extractAutocompleteData(data);
        this.initAutocompleteStreams();
        this.errorMessage.set('')
      },
      error: err => {
        this.errorMessage.set('Failed to load jobs');
        console.error('API Error: ', err);
      }
    });
  }

  private extractAutocompleteData(jobs: Job[]) {
    const clients = jobs.map(j => j.client?.lastName).filter((v): v is string => !!v);
    this.uniqueClients = Array.from(new Set(clients));

    const companies = jobs.map(j => j.primeContractor?.companyName).filter((v): v is string => !!v);
    this.uniqueCompanies = Array.from(new Set(companies));
  }

  private initAutocompleteStreams(): void {
    this.filteredClients = this.clientCtrl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '', this.uniqueClients))
    );

    this.filteredCompanies = this.companyCtrl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '', this.uniqueCompanies))
    );
  }
  private _filter(value: string, source: string[]): string[] {
    const filterValue = value.toLowerCase();
    return source.filter(option => option.toLowerCase().includes(filterValue));
  }

  private autocompleteValidator(getCollection: () => string[]) {
    return (control: AbstractControl) => {
      const selection = control.value;
      if (!selection) return null; // Let empty field pass (no active filter)

      const collection = getCollection();
      const isValid = collection.some(option => option.toLowerCase() === selection.toLowerCase());

      return isValid ? null : { invalidAutocompleteSelection: true };
    };
  }




  //filter search

  onSearch(){
    if (this.clientCtrl.invalid || this.companyCtrl.invalid) {
      this.errorMessage.set('Please select valid search parameters from the autocomplete options dropdown list.');
      return;
    }

    this.isLoading.set(true);
    const clientValue = this.clientCtrl.value;
    const companyValue = this.companyCtrl.value;

    const cleanFilters: { lastName?: string; companyName?: string; status?: JobStatus } = {};

    if(clientValue && clientValue.trim()){
      cleanFilters.lastName = clientValue.trim();
    }
    if(companyValue && companyValue.trim()){
      cleanFilters.companyName = companyValue.trim();
    }
    if(this.statusFilter){
      cleanFilters.status = this.statusFilter as JobStatus;
    }

    this.jobService.searchJobs(cleanFilters).subscribe({
      next: (filteredData: Job[]) => {
        this.jobsList.set(filteredData);
        this.errorMessage.set('')
        this.isLoading.set(false);
      },
      error: err => {
        console.error('API Error: ', err);
        this.errorMessage.set('search failed');
        this.isLoading.set(false);
      }
    });
  }

  //reset filters
  onClearFilters(){
    this.clientCtrl.setValue('');
    this.companyCtrl.setValue('');
    this.statusFilter = '';
    this.loadAllJobs();
  }



  startJob(jobId: number) {
    this.jobService.startJob(jobId).subscribe({
      next: () => this.onSearch(),
      error: err => console.error('failed to start project',err)
    });
  }



}
