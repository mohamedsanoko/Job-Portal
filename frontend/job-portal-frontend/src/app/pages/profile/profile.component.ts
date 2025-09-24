import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { Profile, ProfileService } from '../../core/services/profile.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  form = this.fb.group({
    bio: [''],
    skills: [''],
    experience: [''],
    location: ['']
  });
  profile: Profile | null = null;
  loading = false;
  message = '';

  constructor(private fb: FormBuilder, private profileService: ProfileService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileService.getProfile().subscribe(profile => {
      this.profile = profile;
      this.form.patchValue(profile);
    });
  }

  save(): void {
    this.loading = true;
    this.profileService.updateProfile(this.form.value).pipe(finalize(() => this.loading = false)).subscribe(profile => {
      this.profile = profile;
      this.message = 'Profile updated successfully!';
    });
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) {
      return;
    }
    this.profileService.uploadResume(file).subscribe(profile => {
      this.profile = profile;
      this.message = 'Resume uploaded and scanned successfully.';
    });
  }
}