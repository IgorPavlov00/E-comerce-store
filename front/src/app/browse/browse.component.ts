import { Component } from '@angular/core';

@Component({
  selector: 'app-browse',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.css']
})
export class BrowseComponent {

  isFilterMenuActive: boolean = false;
  isGridActive: boolean = true;

  toggleFilterMenu(): void {
    this.isFilterMenuActive = !this.isFilterMenuActive;
  }

  switchToGrid(): void {
    this.isGridActive = true;
  }

  switchToList(): void {
    this.isGridActive = false;
  }

  toggleLightMode(): void {
    document.documentElement.classList.toggle('light');
  }
}
