import {Component, HostBinding} from '@angular/core';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-homepage',
  imports: [
    RouterLink
  ],
  templateUrl: './homepage.html',
  styleUrl: './homepage.css',
})
export class Homepage {
  isDarkMode = false;

  @HostBinding('class.isDarkMode') get modeClass() {
    return this.isDarkMode;
  }

  toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;
  }


}
