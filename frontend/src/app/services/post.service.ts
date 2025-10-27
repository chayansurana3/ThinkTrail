import { Injectable } from '@angular/core';
import { Observable, throwError, of } from 'rxjs';
import { environment } from '../environments/environment';
import { tap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { StateService } from './state.service';
import { PostsResponse } from '../responses/postsResponse.model';
import { createPostRequest } from '../requests/createPostRequest.model';
import { createPostResponse } from '../responses/createPostResponse.model';
import { Post } from '../models/post.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  private apiUrl = environment.postsApiUrl;

  constructor(private http: HttpClient, private state: StateService) {}

  getAllPosts() {
    return this.http.get<PostsResponse>(`${this.apiUrl}/getAll`).pipe(
      tap((res) => {
        console.log('Found These Posts: ', res);
        this.handlePostsResponse(res);
      }),
      catchError((error: any) => {
        this.state.setPostsError(error.message || 'Posts fetching failed');
        return throwError(() => error);
      })
    );
  }

  private handlePostsResponse(res: PostsResponse) {
    this.state.setAllPosts(res.posts);
  }

  getPostById(id: number): Observable<Post> {
    const posts = this.state.allPosts;
    const post = posts.find((p) => p.id === id);

    if (post) {
      return of(post);
    } else {
      return throwError(() => new Error(`Post with ID ${id} not found`));
    }
  }

  createPost(req: createPostRequest) {
    return this.http
      .post<createPostResponse>(`${this.apiUrl}/create`, req, {
        headers: { 'Content-Type': 'application/json' },
      })
      .pipe(
        tap((res) => {
          if (res.success) {
            this.state.addPost(res.post);
          }
        }),
        catchError((error: any) => {
          this.state.setPostsError(
            error.message || 'Unable to create this blog!! Try again later'
          );
          return throwError(() => error);
        })
      );
  }

  deletePostById(postId: number) {
    return this.http.delete<void>(`${this.apiUrl}/${postId}`).pipe(
      tap(() => {
        this.state.removePost(postId);
      }),
      catchError((error: any) => {
        this.state.setPostsError(error.message || 'Failed to delete post');
        return throwError(() => error);
      })
    );
  }
}
