import { Component, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { StateService } from '../../services/state.service';
import { User } from '../../models/user.model';
import { Observable } from 'rxjs';
import { AVATARS } from '../../models/avatars.model';

@Component({
  standalone: true,
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  imports: [CommonModule, RouterModule],
})
export class NavbarComponent {
  isLogin: boolean = false;
  user: User | null = null;
  user$!: Observable<User | null>;
  Name: string = 'My Account';
  avatars = AVATARS;
  avatarUrl: string = this.avatars[0];
  isDropdownOpen = false;

  constructor(
    private state: StateService,
    private auth: AuthService,
    private el: ElementRef
  ) {
    this.state.isLogin$.subscribe((isLogin) => (this.isLogin = isLogin));
    this.state.currentUser$.subscribe((user) => {
      this.user = user;
      this.Name =
        (this.user?.firstName ? this.user?.firstName : '') +
        ' ' +
        (this.user?.middleName ? this.user?.middleName : '') +
        ' ' +
        (this.user?.lastName ? this.user?.lastName : '');
      if (this.Name.trim() === '') this.Name = 'My Account';
      const index = Number(this.user?.avatar ?? 0);
      this.avatarUrl = this.avatars[index] || this.avatars[0];
    });
    this.user$ = this.state.currentUser$;
  }

  get isDarkMode() {
    return this.state.currentTheme === 'dark';
  }

  getAvatarUrl(): string {
    const index = Number(this.user?.avatar ?? 0);
    return this.avatars[index];
  }

  toggleTheme() {
    this.state.toggleTheme();
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: Event) {
    const clickedInside = this.el.nativeElement.contains(event.target);
    if (!clickedInside) {
      this.closeDropdown();
    }
  }

  logout() {
    let logout = window.confirm('Do you want to logout ?');
    if (logout) this.auth.logout();
  }
}
