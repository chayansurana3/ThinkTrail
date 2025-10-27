import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';
import { environment } from '../environments/environment';
import { environmentPRD } from '../environments/environment.prod';

@Injectable({ providedIn: 'root' })
export class CommentService {
  // private api = environment.commentsApiUrl;
  private api = environmentPRD.baseApiUrl + "/comments";

  constructor(private http: HttpClient) {}

  getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.api}/post/${postId}`);
  }

  getCommentsByUserId(userId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.api}/user/${userId}`);
  }  

  addComment(postId: number, userId: number, content: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.api}/${postId}`, { userId, content });
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${commentId}`);
  }
}