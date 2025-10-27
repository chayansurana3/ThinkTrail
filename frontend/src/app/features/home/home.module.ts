import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    HomeComponent,
    RouterModule.forChild([{ path: '', component: HomeComponent }])
  ]
})
export class HomeModule {}