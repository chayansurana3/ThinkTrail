import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentComponent } from './comment.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [ CommonModule, CommentComponent ]
})

export class CommentModule {}