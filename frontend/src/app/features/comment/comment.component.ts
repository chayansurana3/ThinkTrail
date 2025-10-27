import { Component, OnInit, Input } from '@angular/core';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment.model';
import { StateService } from '../../services/state.service';
import { FormsModule, NgForm } from '@angular/forms';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AVATARS } from '../../models/avatars.model';

@Component({
  selector: 'app-comments',
  imports: [FormsModule, NgIf, NgFor, RouterModule, DatePipe],
  templateUrl: './comment.component.html',
})
export class CommentComponent implements OnInit {
  comments: Comment[] = [];
  newComment: string = '';
  userId: any;
  @Input() postId!: number; // receives from PostComponent

  constructor(
    private commentService: CommentService,
    private state: StateService
  ) {}

  ngOnInit() {
    this.state.currentUser$.subscribe((user) => {
      this.userId = user?.id;
      this.loadComments();
    });
  }

  loadComments(): void {
    this.commentService.getCommentsByPostId(this.postId).subscribe((res) => {
      this.comments = res;
      console.log(res);
    });
  }

  addComment(): void {
    if (!this.newComment.trim()) return;

    this.commentService
      .addComment(this.postId, this.userId, this.newComment)
      .subscribe({
        next: (comment) => {
          // Comment successfully added
          this.comments.push(comment);
          this.newComment = '';
        },
        error: (err) => {
          // Handle moderation or server errors
          if (err.status === 422) {
            // Backend flagged the content
            const errorMessage =
              err.error?.error || 'Your comment was flagged as inappropriate.';
            alert(`⚠️ Comment rejected: ${errorMessage}`);
          } else if (err.status === 500) {
            // Server issue (e.g., API failure)
            const details = err.error?.details || 'Internal moderation error.';
            alert(`❌ Failed to process comment: ${details}`);
          } else {
            // Generic fallback
            alert('An unexpected error occurred. Please try again later.');
          }

          console.error('Error adding comment:', err);
        },
      });
  }

  deleteComment(commentId: number): void {
    const confirmed = window.confirm(
      'Are you sure you want to delete this comment?'
    );
    if (!confirmed) return;

    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.comments = this.comments.filter((c) => c.id !== commentId);
      },
      error: (err) => {
        console.error('Error deleting comment', err);
        alert('Failed to delete comment. Please try again.');
      },
    });
  }

  // Add a small helper to get avatar URLs
  getCommentAvatar(c: Comment): string {
    return this.getAvatarByIndex(c.avatar)
  }

  // Current logged-in user avatar
  userAvatarUrl(): string {
    return this.getAvatarByIndex(Number(this.state.currentUser?.avatar))
  }

  // Helper to fetch avatar by index
  getAvatarByIndex(index: number): string {
    const avatars = AVATARS;
    return avatars[index];
  }
}
