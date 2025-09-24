import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './pages/landing/landing-page.component';
import { LoginComponent } from './pages/auth/login.component';
import { RegisterComponent } from './pages/auth/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ApplicationsComponent } from './pages/applications/applications.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { EmployerDashboardComponent } from './pages/employer/employer-dashboard.component';
import { EmployerJobFormComponent } from './pages/employer/job-form/employer-job-form.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard.component';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'applications', component: ApplicationsComponent, canActivate: [RoleGuard], data: { roles: ['JOB_SEEKER'] } },
  { path: 'favorites', component: FavoritesComponent, canActivate: [RoleGuard], data: { roles: ['JOB_SEEKER'] } },
  { path: 'employer', component: EmployerDashboardComponent, canActivate: [RoleGuard], data: { roles: ['EMPLOYER'] } },
  { path: 'employer/jobs/new', component: EmployerJobFormComponent, canActivate: [RoleGuard], data: { roles: ['EMPLOYER'] } },
  { path: 'employer/jobs/:id', component: EmployerJobFormComponent, canActivate: [RoleGuard], data: { roles: ['EMPLOYER'] } },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [RoleGuard], data: { roles: ['ADMIN'] } },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}