import { Component, signal } from '@angular/core';
import { RouterOutlet,RouterModule } from '@angular/router';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatListItem, MatNavList} from '@angular/material/list';
import {MatIconButton} from '@angular/material/button';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, MatToolbar, MatIcon, MatSidenavContent, MatNavList, MatSidenav, MatSidenavContainer, MatIconButton, MatListItem],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
