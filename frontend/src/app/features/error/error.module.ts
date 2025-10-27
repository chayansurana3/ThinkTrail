import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { ErrorComponent } from './error.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    ErrorComponent,
    RouterModule.forChild([{ path: 'error', component: ErrorComponent }])
  ]
})
export class ErrorModule {}