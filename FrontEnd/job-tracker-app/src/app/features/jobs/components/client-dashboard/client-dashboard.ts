import { Component } from '@angular/core';
import {ClientTable} from '../client-table/client-table';

@Component({
  selector: 'app-client-dashboard',
  imports: [
    ClientTable
  ],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.css',
})
export class ClientDashboard {}
