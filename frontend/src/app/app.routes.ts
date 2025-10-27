import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { PostsComponent } from './features/posts/posts.component';
import { WriteComponent } from './features/write/write.component';
import { LoginComponent } from './features/login/login.component';
import { PostComponent } from './features/post/post.component';
import { MyPostsComponent } from './features/user-posts/myposts.component';
import { SignupComponent } from './features/signup/signup.component';
import { ProfileComponent } from './features/profile/profile.component';
import { AuthGuard } from '../app/guards/auth.guard';
import { ErrorComponent } from './features/error/error.component';
import { UserCommentsComponent } from './features/user-comments/user-comments.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
    },
    {
        path: 'posts',
        component: PostsComponent
    },
    {
        path: 'write',
        component: WriteComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'posts/:id',
        component: PostComponent,
        canActivate: [AuthGuard]
    },
    { 
        path: 'myposts', 
        component: MyPostsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'signup',
        component: SignupComponent
    },
    {
        path: 'profile/:id',
        component: ProfileComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'userComments',
        component: UserCommentsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'error',
        component: ErrorComponent
    },
    { 
        path: '**', 
        redirectTo: '' 
    }  
];
