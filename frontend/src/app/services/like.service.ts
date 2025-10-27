import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment';
import { tap, catchError, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { StateService } from './state.service';
import { environmentPRD } from '../environments/environment.prod';
@Injectable({ providedIn: 'root' })
export class LikeService {
  // private apiUrl = environment.likesApiUrl;
  private apiUrl = environmentPRD.baseApiUrl + "/likes";

  constructor(private http: HttpClient, private state: StateService) {}

  likePost(userId: number, postId: number): Observable<any> {
    const url = `${this.apiUrl}?userId=${userId}&postId=${postId}`;
    return this.http.post(url, {}).pipe(
      tap(() => {
        this.state.addLikedPost(postId);
        const post = this.state.allPosts.find((p) => p.id === postId);
        if (post) this.state.updatePostLikes(postId, post.likes + 1);
      }),
      catchError((error) => throwError(() => error))
    );
  }

  unlikePost(userId: number, postId: number): Observable<any> {
    const url = `${this.apiUrl}?userId=${userId}&postId=${postId}`;
    return this.http.delete(url).pipe(
      tap(() => {
        this.state.removeLikedPost(postId);
        const post = this.state.allPosts.find((p) => p.id === postId);
        if (post) this.state.updatePostLikes(postId, post.likes - 1);
      }),
      catchError((error) => throwError(() => error))
    );
  }

  isLiked(userId: number, postId: number): Observable<boolean> {
    const url = `${this.apiUrl}/${userId}/${postId}`;
    return this.http.get(url, { observe: 'response' }).pipe(
      map((response) => response.status === 200),
      catchError((error) => {
        console.log("This post is not liked by you")
        if (error.status === 404) {
          return [false];
        }
        return throwError(() => error);
      })
    );
  }
}