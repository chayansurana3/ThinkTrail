import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { updateUserRequest } from '../requests/updateUserRequest.model';
import { StateService } from './state.service';
import { HttpClient } from '@angular/common/http';
import { updateUserResponse } from '../responses/updateUserResponse.model';
import { tap, catchError } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { environmentPRD } from '../environments/environment.prod';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  // private apiUrl = environment.usersApiUrl;
  private apiUrl = environmentPRD.baseApiUrl + "/users";

  constructor(
    private http: HttpClient,
    private state: StateService,
    private router: Router
  ) {}

  updateUser(req: updateUserRequest) {
    return this.http
      .post<updateUserResponse>(`${this.apiUrl}/update`, req, {
        headers: { 'Content-Type': 'application/json' },
      })
      .pipe(
        tap((res) => this.state.setCurrentUser(res.userDTO)),
        catchError((error: any) => {
          return throwError(() => error);
        })
      );
  }

  getUserById(userId: number) {
    return this.http.get<User>(`${this.apiUrl}/${userId}`);
  }

  followUser(userId: number, targetId: number): Observable<User> {
    return this.http
      .post<User>(`${this.apiUrl}/${userId}/follow/${targetId}`, {})
      .pipe(
        tap(() => {
          // Refresh current user state after follow
          this.getUserById(userId).subscribe((updatedUser) => {
            this.state.setCurrentUser(updatedUser);
            alert('User followed successfully!!');
          });
        }),
        catchError((error) => {
          alert('Failed to follow user. Please try again!');
          return throwError(() => error);
        })
      );
  }

  unfollowUser(userId: number, targetId: number): Observable<User> {
    return this.http
      .delete<User>(`${this.apiUrl}/${userId}/unfollow/${targetId}`)
      .pipe(
        tap(() => {
          // Refresh current user state after unfollow
          this.getUserById(userId).subscribe((updatedUser) => {
            this.state.setCurrentUser(updatedUser);
            alert('User unfollowed successfully!!');
          });
        }),
        catchError((error) => {
          alert('Failed to unfollow user. Please try again!');
          return throwError(() => error);
        })
      );
  }
}
