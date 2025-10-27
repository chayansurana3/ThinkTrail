import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule, NgIf, NgForOf } from '@angular/common';
import { PostService } from '../../services/post.service';
import { LikeService } from '../../services/like.service';
import { StateService } from '../../services/state.service';
import { UserService } from '../../services/user.service';
import { CommentComponent } from '../comment/comment.component';
import { environment } from '../../environments/environment';
import { AVATARS } from '../../models/avatars.model';
import { Subject, takeUntil } from 'rxjs';
import { Post } from '../../models/post.model';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';
type PostWithAuthor = Post & { author?: User };

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, CommentComponent, NgIf, NgForOf, RouterModule],
  templateUrl: './post.component.html',
})
export class PostComponent implements OnInit, OnDestroy {
  post: PostWithAuthor | null = null;
  author: User | null = null; // duplicate reference for convenience
  userId: number | null = null;

  liked = false;
  likesCount = 0;
  api = environment.likesApiUrl;

  animating = false;
  floatingHearts: Array<{ id: number; offset: number; delay: number }> = [];

  // hover card state
  hoveredUser: User | null = null;
  showUserCard = false;
  hoverTimeout: any;
  hideTimeout: any;

  userCache = new Map<number, User>();
  avatars = AVATARS;

  private destroyed$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private likeService: LikeService,
    private state: StateService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // read id from route and load post
    this.route.paramMap.pipe(takeUntil(this.destroyed$)).subscribe((params) => {
      const idParam = params.get('id');
      const id = idParam ? Number(idParam) : NaN;
      if (!id || isNaN(id)) {
        console.error('Invalid post id in route');
        return;
      }
      this.loadPost(id);
    });

    // subscribe to current user for like actions
    this.state.currentUser$
      .pipe(takeUntil(this.destroyed$))
      .subscribe((user) => {
        this.userId = user?.id ?? null;

        // if post already present, set liked state
        if (this.post?.id && this.userId) {
          this.liked = this.state.likedPosts.includes(this.post.id);
          if (!this.liked) this.checkIfLikedFromApi();
        }
      });
  }

  private loadPost(id: number): void {
    this.postService
      .getPostById(id)
      .pipe(takeUntil(this.destroyed$))
      .subscribe({
        next: (post) => {
          // attach post (immutable fields come from Post interface)
          this.post = post as PostWithAuthor;
          this.likesCount = post.likes ?? 0;

          // fetch author using authorId
          if (post.authorId) {
            const cached = this.userCache.get(post.authorId);
            if (cached) {
              this.author = cached;
              this.post.author = cached;
            } else {
              this.userService
                .getUserById(post.authorId)
                .pipe(takeUntil(this.destroyed$))
                .subscribe({
                  next: (user) => {
                    this.author = user;
                    this.userCache.set(post.authorId, user);
                    if (this.post) this.post.author = user;
                  },
                  error: (err) =>
                    console.error('Error fetching author details', err),
                });
            }
          }
        },
        error: (err) => {
          console.error('Error fetching post:', err);
        },
      });
  }

  private checkIfLikedFromApi(): void {
    if (!this.userId || !this.post?.id) return;
    this.likeService
      .isLiked(this.userId, this.post.id)
      .pipe(takeUntil(this.destroyed$))
      .subscribe({
        next: (liked) => {
          this.liked = liked;
          if (liked) this.state.addLikedPost(this.post!.id);
        },
        error: (err) => console.error('Error checking like status', err),
      });
  }

  toggleLike(): void {
    if (!this.userId || !this.post?.id) {
      console.warn('User or post not available for like action');
      return;
    }

    if (!this.liked) {
      this.likeService
        .likePost(this.userId, this.post.id)
        .pipe(takeUntil(this.destroyed$))
        .subscribe({
          next: () => {
            this.likesCount++;
            this.liked = true;
            this.state.addLikedPost(this.post!.id);

            // animation and floating hearts
            this.animating = true;
            setTimeout(() => (this.animating = false), 300);

            for (let i = 0; i < 3; i++) {
              const id = Date.now() + i;
              const offset = (Math.random() - 0.5) * 80;
              const delay = i * 150;
              this.floatingHearts.push({ id, offset, delay });
              setTimeout(() => {
                this.floatingHearts = this.floatingHearts.filter(
                  (h) => h.id !== id
                );
              }, 1000);
            }
          },
          error: (err) => console.error('Error liking post:', err),
        });
    } else {
      this.likeService
        .unlikePost(this.userId, this.post.id)
        .pipe(takeUntil(this.destroyed$))
        .subscribe({
          next: () => {
            this.likesCount = Math.max(0, this.likesCount - 1);
            this.liked = false;
            this.state.removeLikedPost(this.post!.id);
          },
          error: (err) => console.error('Error unliking post:', err),
        });
    }
  }

  onAuthorHover(): void {
    this.hoverTimeout = setTimeout(() => {
      const user = this.post?.author ?? this.author;
      if (user) {
        this.hoveredUser = user;
        this.showUserCard = true;
      }
    }, 300);
  }

  onAuthorLeave(): void {
    clearTimeout(this.hoverTimeout);
    this.hideTimeout = setTimeout(() => (this.showUserCard = false), 200);
  }

  getAvatar(user: User | null | undefined): string {
    const idx = user?.avatar ?? 0;
    return this.avatars[Number(idx)] || this.avatars[0];
  }

  clearHideTimeout(): void {
    if (this.hideTimeout) {
      clearTimeout(this.hideTimeout);
      this.hideTimeout = null;
    }
  }

  deletePost(): void {
    if (!this.post) return;

    const confirmed = confirm('Are you sure you want to delete this post? This action cannot be undone.');
    if (!confirmed) return;

    this.postService.deletePostById(this.post.id).subscribe({
      next: () => {
        alert('Post deleted successfully!');
        this.router.navigate(['/posts']);
      },
      error: (err) => {
        console.error('Error deleting post:', err);
        alert('Failed to delete post. Try again later.');
      },
    });
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
    clearTimeout(this.hoverTimeout);
    clearTimeout(this.hideTimeout);
  }
}
