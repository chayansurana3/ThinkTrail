import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './signup.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    SignupComponent,
    RouterModule.forChild([{ path: 'signup', component: SignupComponent }])
  ]
})
export class SignupModule {}