import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostsComponent } from './posts.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    PostsComponent,
    RouterModule.forChild([{ path: 'posts', component: PostsComponent }])
  ]
})
export class HomeModule {}