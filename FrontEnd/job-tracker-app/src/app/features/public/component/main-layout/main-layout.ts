import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Footer} from '../footer/footer';
import {NavBar} from '../nav-bar/nav-bar';

@Component({
  selector: 'app-main-layout',
  imports: [
    RouterOutlet,
    Footer,
    NavBar
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {}
