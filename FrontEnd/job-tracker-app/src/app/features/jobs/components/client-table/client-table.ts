import {ChangeDetectorRef, Component, OnInit, signal} from '@angular/core';
import {Client} from '../../models/client.model';
import {ClientService} from '../../services/client';
import {Router, RouterLink} from '@angular/router';
import {Vendor} from '../../../contractors/models/vendor.model';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-client-table',
  standalone: true,
  imports: [
    MatIcon,
    MatIconButton,
    MatCell,
    MatHeaderCell,
    MatColumnDef,
    MatTable,
    RouterLink,
    MatButton,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
    NgIf
  ],
  templateUrl: './client-table.html',
  styleUrl: './client-table.css',
})
export class ClientTable implements OnInit {
  clientList = signal<Client[]>([]);
  errorMessage = '';

  displayedColumns: string[] = [
    'clientId',
    'firstName',
    'lastName',
    'address',
    'phoneNumber',
    'actions'
  ];

  constructor(private clientService: ClientService,
              private cdr: ChangeDetectorRef,
              private router: Router,) {}

  ngOnInit() {
    this.loadClientData();
  }

  loadClientData() {
    this.clientService.getClients().subscribe({
      next: (clients: Client[]) => {
        this.clientList.set(clients);
        this.cdr.detectChanges();
      }
    });
  }

  onEdit(client: Client) {
    this.router.navigate(['/clients/new',client.clientId])
  }

  onDelete(client: Client) {
    if(confirm('Are you sure you want to delete?')) {
      this.clientService.deleteClient(client.clientId!).subscribe({
        next: () => {
          this.loadClientData();
        },
        error: error => {
          console.error(error);
          this.cdr.detectChanges();
        }
      });
    }
  }





}
