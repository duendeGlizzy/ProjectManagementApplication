import { Component, signal } from '@angular/core';
import { RouterOutlet,RouterModule } from '@angular/router';
import {MatToolbar} from '@angular/material/toolbar';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, MatToolbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
