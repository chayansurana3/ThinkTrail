import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { Post } from '../../models/post.model';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { StateService } from '../../services/state.service';

@Component({
  standalone: true,
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  imports: [CommonModule],
})
export class PostsComponent {
  posts$: Observable<Post[]>; 

  constructor(private router: Router, private postService: PostService, private state: StateService) {
    this.posts$ = this.postService.getAllPosts().pipe(
      map((res) => res.posts) 
    );
    this.posts$.subscribe({
      next: (val) => console.log("Posts emitted:", val),
      error: (err) => console.error("Error in posts$:", err),
      complete: () => console.log("posts$ completed"),
    });
  }

  navigateToPost(id: number) {
    this.router.navigate(['/posts', id]);
  }

  onImageError(event: Event) {
    const target = event.target as HTMLImageElement;
    target.src = 'assets/default-image.png';
  }
}