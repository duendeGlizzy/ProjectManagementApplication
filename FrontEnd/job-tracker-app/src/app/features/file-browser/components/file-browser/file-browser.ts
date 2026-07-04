import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatList, MatListItem} from '@angular/material/list';
import {FileBrowserService} from '../../services/file-browser-service';
import {MatIconButton} from '@angular/material/button';
import {NgForOf} from '@angular/common';


@Component({
  selector: 'app-file-browser',
  standalone: true,
  imports: [
    MatCardHeader,
    MatCard,
    MatIcon,
    MatCardContent,
    MatList,
    MatListItem,
    MatIconButton,
    MatCardTitle,
    NgForOf
  ],
  templateUrl: './file-browser.html',
  styleUrl: './file-browser.css',
})
export class FileBrowser implements OnInit {


  constructor(private fileBrowserService: FileBrowserService) {
  }

  protected files: string[] = [];
  protected loading= true;
  protected errorMessage = '';

  ngOnInit() {
    this.loadFiles();
  }


  loadFiles(): void {
    this.loading = true;
    this.errorMessage = '';

    this.fileBrowserService.getFiles().subscribe({
      next: (data) => {
        this.files = data || [];
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'failed to load storage';
        this.loading = false;
        console.error('failed to load storage', error);
      }
    });
  }

  viewFile(filename: string): void {
    this.fileBrowserService.getFileDownloadUrl(filename).subscribe({
      next: (res) => {
        if(res && res.url){
          window.open(res.url, '_blank');
        }
      },
      error: (error) => {
        alert('could not generate secure access');
        console.error('aws presign generation exception', error);
      }
    });
  }

}

