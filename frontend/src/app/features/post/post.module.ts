import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostComponent } from './post.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    PostComponent,
    RouterModule.forChild([{ path: 'posts/:id', component: PostComponent }])
  ]
})
export class PostModule {}