import {ChangeDetectorRef, Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardModule, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatList, MatListItem} from '@angular/material/list';
import {FileBrowserService, StorageItem} from '../../services/file-browser-service';
import {MatButton, MatIconButton} from '@angular/material/button';
import {NgForOf, NgIf} from '@angular/common';
import {MatProgressBar} from '@angular/material/progress-bar';


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
    NgForOf,
    MatButton,
    MatProgressBar,
    NgIf
  ],
  templateUrl: './file-browser.html',
  styleUrl: './file-browser.css',
})
export class FileBrowser implements OnInit {

  @ViewChild('fileInput') public fileInput!: ElementRef;

  items: StorageItem[] = [];
  pathHistory: string[] = [];
  currentPrefix: string = '';
  isLoading: boolean = false;

  cdr = inject(ChangeDetectorRef);


  constructor(private fileBrowserService: FileBrowserService) {
  }

  protected files: string[] = [];
  protected loading= true;
  protected errorMessage = '';

  ngOnInit() {
    this.loadDirectoryContents();
  }

  loadDirectoryContents() {
    this.isLoading = true;
    this.fileBrowserService.getContents(this.currentPrefix).subscribe({
      next: (data) => {
        this.items = [...data.folders, ...data.files];
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error fetching data', error);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onItemClick(item: StorageItem) {
    if(item.isFolder){
      this.pathHistory.push(item.name);
      this.updatePrefixAndReload();
    }else{
      this.viewFile(item.fullName);
    }
  }

  navigateBackTo(index: number) {
    this.pathHistory = this.pathHistory.slice(0, index + 1);
    this.updatePrefixAndReload();
  }

  navigateToRoot(){
    this.pathHistory = [];
    this.updatePrefixAndReload();
  }

  private updatePrefixAndReload() {
    this.currentPrefix = this.pathHistory.length > 0 ? this.pathHistory.join('/') + '/' : "";
    this.loadDirectoryContents();
  }

  triggerFileUpload() {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    if(element.files && element.files.length > 0){
      this.isLoading = true;
      this.fileBrowserService.uploadFile(element.files[0], this.currentPrefix).subscribe({
        next: () => {
          this.loadDirectoryContents();
          element.value = '';
        },
        error: (error) => {
          console.error('Upload failed', error);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteFile(key: string, event: Event) {
    event.stopPropagation();
    if(confirm('Are you sure you want to delete this file?')) {
      this.isLoading = true;
      this.fileBrowserService.deleteFile(key).subscribe({
        next: () => this.loadDirectoryContents(),
        error: (error) => {
          console.error('Error deleting file', error);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }


  viewFile(key: string){
    this.fileBrowserService.getDownloadLink(key).subscribe({
      next: (res) => window.open(res.url, '_blank'),
      error: (error) => console.error('Presigning', error)
    });
  }

}

