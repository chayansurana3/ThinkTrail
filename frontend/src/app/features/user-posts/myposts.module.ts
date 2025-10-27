import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MyPostsComponent } from './myposts.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    MyPostsComponent,
    RouterModule.forChild([{ path: 'myposts/:id', component: MyPostsComponent }])
  ]
})
export class MyPostsModule {}