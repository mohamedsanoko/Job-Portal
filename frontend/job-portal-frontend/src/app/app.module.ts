import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingPageComponent } from './pages/landing/landing-page.component';
import { LoginComponent } from './pages/auth/login.component';
import { RegisterComponent } from './pages/auth/register.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ApplicationsComponent } from './pages/applications/applications.component';
import { FavoritesComponent } from './pages/favorites/favorites.component';
import { EmployerDashboardComponent } from './pages/employer/employer-dashboard.component';
import { EmployerJobFormComponent } from './pages/employer/job-form/employer-job-form.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard.component';
import { TopNavComponent } from './shared/components/top-nav/top-nav.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    ApplicationsComponent,
    FavoritesComponent,
    EmployerDashboardComponent,
    EmployerJobFormComponent,
    AdminDashboardComponent,
    TopNavComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}