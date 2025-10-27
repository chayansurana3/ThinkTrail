import { Component, OnInit } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { StateService } from '../../services/state.service';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { PostsResponse } from '../../responses/postsResponse.model';
import { Post } from '../../models/post.model';

@Component({
  standalone: true,
  selector: 'app-myposts',
  templateUrl: './myposts.component.html',
  imports: [CommonModule, NgFor]
})

export class MyPostsComponent implements OnInit {
  posts$: Observable<Post[]> = new Observable<Post[]>(); // filtered posts for template

  constructor(
    private router: Router,
    private postService: PostService,
    private state: StateService
  ) {}

  ngOnInit(): void {
    this.posts$ = this.state.currentUser$.pipe(
      switchMap(userId =>
        this.postService.getAllPosts().pipe(
          map((res: PostsResponse) =>
            res.posts.filter(post => post.authorId === userId?.id)
          )
        )
      )
    );
  }

  navigateToPost(id: number) {
    this.router.navigate(['/posts', id]);
  }
}