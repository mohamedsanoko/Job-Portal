import { Component, OnInit } from '@angular/core';
import { Job } from '../../core/models/job.model';
import { JobService } from '../../core/services/job.service';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})
export class FavoritesComponent implements OnInit {
  favorites: Job[] = [];

  constructor(private jobService: JobService) {}

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.jobService.favorites().subscribe(favorites => this.favorites = favorites);
  }

  remove(job: Job): void {
    this.jobService.toggleFavorite(job.id).subscribe(() => this.loadFavorites());
  }
}