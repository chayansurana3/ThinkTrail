import { Component, Input, OnInit, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment.model';
import { StateService } from '../../services/state.service';

@Component({
  selector: 'app-user-comments',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-comments.component.html',
})
export class UserCommentsComponent implements OnInit {
  @Input() userId!: number; // user whose comments we are fetching
  comments: Comment[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private commentService: CommentService,
    private state: StateService,
    private router: Router
  ) {}

  ngOnInit() {
    this.state.currentUser$.subscribe((user) => {
      if (user && user.id) {
        this.fetchComments(user.id);
      }
    });
  }

  fetchComments(userId: number) {
    this.loading = true;
    this.commentService.getCommentsByUserId(userId).subscribe({
      next: (data) => {
        this.comments = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching comments:', err);
        this.loading = false;
      },
    });
  }

  deleteComment(commentId: number) {
    if (!confirm('Are you sure you want to delete this comment?')) return;

    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.comments = this.comments.filter((c) => c.id !== commentId);
      },
      error: (err) => {
        console.error('Error deleting comment:', err);
        this.errorMessage = 'Could not delete comment.';
      },
    });
  }
}