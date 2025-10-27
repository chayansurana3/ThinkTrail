import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    ProfileComponent,
    RouterModule.forChild([{ path: ':id', component: ProfileComponent }])
  ]
})

export class ProfileModule {}