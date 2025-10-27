import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserCommentsComponent } from './user-comments.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    UserCommentsComponent,
    RouterModule.forChild([{ path: 'userComments/', component: UserCommentsComponent }])
  ]
})
export class UserCommentsModule {}