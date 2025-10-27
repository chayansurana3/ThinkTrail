import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';
import { User } from '../models/user.model';
import { Post } from '../models/post.model';

@Injectable({
  providedIn: 'root',
})
export class StateService {
  private isBrowser: boolean;

  private isLoginSubject = new BehaviorSubject<boolean>(false);
  isLogin$ = this.isLoginSubject.asObservable();

  private authTokenSubject = new BehaviorSubject<string | null>(null);
  authToken$ = this.authTokenSubject.asObservable();

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();

  private authErrorSubject = new BehaviorSubject<string | null>(null);
  authError$ = this.authErrorSubject.asObservable();

  private allPostsSubject = new BehaviorSubject<Post[]>([]);
  allPosts$ = this.allPostsSubject.asObservable();

  private postsErrorSubject = new BehaviorSubject<string | null>(null);
  postsError$ = this.postsErrorSubject.asObservable();

  private createPostsErrorSubject = new BehaviorSubject<string | null>(null);
  createPostsError$ = this.createPostsErrorSubject.asObservable();

  private likedPostsSubject = new BehaviorSubject<number[]>([]);
  likedPosts$ = this.likedPostsSubject.asObservable();

  private themeSubject = new BehaviorSubject<'light' | 'dark'>('light');
  theme$ = this.themeSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);

    if (this.isBrowser) {
      const token = localStorage.getItem('auth_token');
      const user = localStorage.getItem('user');
      const posts = localStorage.getItem('posts');
      const likedPosts = localStorage.getItem('liked_posts');

      if (token) {
        this.setAuthToken(token);
        this.setLoginState(true);
        if (user) this.setCurrentUser(JSON.parse(user));
      }

      if (posts) {
        this.allPostsSubject.next(JSON.parse(posts));
      }

      if (likedPosts) {
        this.likedPostsSubject.next(JSON.parse(likedPosts));
      }
      const savedTheme =
        (localStorage.getItem('theme') as 'light' | 'dark') || 'light';
      this.themeSubject.next(savedTheme);
      this.applyTheme(savedTheme);
    }
  }

  /** ---------------- AUTH ---------------- */
  setLoginState(value: boolean) {
    this.isLoginSubject.next(value);
  }

  setAuthToken(token: string | null) {
    this.authTokenSubject.next(token);
    if (this.isBrowser) {
      if (token) localStorage.setItem('auth_token', token);
      else localStorage.removeItem('auth_token');
    }
  }

  setCurrentUser(user: User | null) {
    this.currentUserSubject.next(user);
    if (this.isBrowser) {
      if (user) localStorage.setItem('user', JSON.stringify(user));
      else localStorage.removeItem('user');
    }
  }

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /** ---------------- POSTS ---------------- */
  setAllPosts(posts: Post[]) {
    const current = this.allPostsSubject.value;
    const merged = new Map<number, Post>();
    [...posts, ...current].forEach((post) => {
      if (post && post.id != null) {
        merged.set(post.id, post);
      }
    });
    const updated = Array.from(merged.values());
    this.allPostsSubject.next(updated);

    if (this.isBrowser) {
      localStorage.setItem('posts', JSON.stringify(updated));
    }
  }

  addPost(post: Post) {
    const updated = [...this.allPostsSubject.value, post];
    this.allPostsSubject.next(updated);

    if (this.isBrowser) {
      localStorage.setItem('posts', JSON.stringify(updated));
    }
  }

  removePost(postId: number) {
    const updated = this.allPostsSubject.value.filter((p) => p.id !== postId);
    this.allPostsSubject.next(updated);

    if (this.isBrowser) {
      localStorage.setItem('posts', JSON.stringify(updated));
    }
  }

  clearPosts() {
    this.allPostsSubject.next([]);

    if (this.isBrowser) {
      localStorage.removeItem('posts');
    }
  }

  clearLikedPosts() {
    this.likedPostsSubject.next([]);

    localStorage.removeItem('liked_posts');
  }

  updatePostLikes(postId: number, likeCount: number) {
    const updated = this.allPostsSubject.value.map((p) =>
      p.id === postId ? { ...p, likes: likeCount } : p
    );
    this.allPostsSubject.next(updated);

    if (this.isBrowser) {
      localStorage.setItem('posts', JSON.stringify(updated));
    }
  }

  setLoading(value: boolean) {
    this.loadingSubject.next(value);
  }

  setAuthError(message: string | null) {
    this.authErrorSubject.next(message);
  }

  setPostsError(message: string | null) {
    this.postsErrorSubject.next(message);
  }

  setCreatePostError(message: string | null) {
    this.createPostsErrorSubject.next(message);
  }

  get allPosts(): Post[] {
    console.log(this.allPostsSubject.value);
    return this.allPostsSubject.value;
  }

  clearAuthState() {
    this.setLoginState(false);
    this.setAuthToken(null);
    this.setCurrentUser(null);
    this.setLoading(false);
    this.setAuthError(null);
    this.clearPosts();
    this.clearLikedPosts();
  }

  /** ---------------- LIKES ---------------- */
  isPostLiked(postId: number): boolean {
    return this.likedPostsSubject.value.includes(postId);
  }

  get likedPosts(): number[] {
    return this.likedPostsSubject.value;
  }

  setLikedPosts(postIds: number[]) {
    this.likedPostsSubject.next(postIds);
    if (this.isBrowser) {
      localStorage.setItem('liked_posts', JSON.stringify(postIds));
    }
  }

  addLikedPost(postId: number) {
    const updated = [...this.likedPostsSubject.value, postId];
    this.setLikedPosts(updated);
  }

  removeLikedPost(postId: number) {
    const updated = this.likedPostsSubject.value.filter((id) => id !== postId);
    this.setLikedPosts(updated);
  }

  /** ---------------- UI STATE ---------------- */
  setTheme(theme: 'light' | 'dark') {
    this.themeSubject.next(theme);

    if (this.isBrowser) {
      localStorage.setItem('theme', theme);
      this.applyTheme(theme);
    }
  }

  toggleTheme() {
    const newTheme = this.themeSubject.value === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    if (!this.isBrowser) return;
    const html = document.documentElement;

    // Manually override system-level color scheme
    if (theme === 'dark') {
      html.classList.add('dark');
      html.style.colorScheme = 'dark';
    } else {
      html.classList.remove('dark');
      html.style.colorScheme = 'light';
    }
  }

  get currentTheme(): 'light' | 'dark' {
    return this.themeSubject.value;
  }
}
