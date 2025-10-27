import { Component, OnDestroy } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { StateService } from '../../services/state.service';
import { UserService } from '../../services/user.service';
import { updateUserRequest } from '../../requests/updateUserRequest.model';
import { AVATARS } from '../../models/avatars.model';

@Component({
  standalone: true,
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  imports: [CommonModule, ReactiveFormsModule, NgIf],
})
export class ProfileComponent implements OnDestroy {
  user: any = null; // profile we are visiting
  currentUser: any = null; // logged-in user
  profileImage: string | ArrayBuffer | null = null;

  form: FormGroup;
  isEditMode: boolean = false;
  isOwnProfile: boolean = false;
  isFollowing: boolean = false;

  private subs: Subscription[] = [];

  showAvatarModal: boolean = false;
  selectedAvatarIndex: number = 0;

  avatars = AVATARS;
  
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private state: StateService,
    private userService: UserService
  ) {
    this.form = this.fb.group({
      firstName: [''],
      middleName: [''],
      lastName: [''],
      email: [{ value: '', disabled: true }],
      bio: [''],
      gender: [''],
      age: [''],
      avatar: [0],
    });

    // get profileId from route
    const sub1 = this.route.paramMap.subscribe((params) => {
      const profileId = Number(params.get('id'));
      if (profileId) this.loadProfile(profileId);
    });

    // keep track of current logged-in user
    const sub2 = this.state.currentUser$.subscribe((cu) => {
      this.currentUser = cu;
      if (this.user) {
        this.checkOwnershipAndFollow();
        if (this.isOwnProfile) {
          this.profileImage = this.avatars[this.currentUser?.avatar ?? 0];
        }
      }
    });

    this.subs.push(sub1, sub2);
  }

  openAvatarModal() {
    this.selectedAvatarIndex = this.form.value.avatar ?? 0;
    this.showAvatarModal = true;
  }

  closeAvatarModal() {
    this.showAvatarModal = false;
  }

  selectAvatar(index: number) {
    this.selectedAvatarIndex = index;
  }

  saveSelectedAvatar() {
    this.form.patchValue({ avatar: this.selectedAvatarIndex });
    this.profileImage = this.avatars[this.selectedAvatarIndex];
    this.closeAvatarModal();
  }

  private checkOwnershipAndFollow() {
    if (!this.user || !this.currentUser) return;
    this.isOwnProfile = this.currentUser.id === this.user.id;
    this.isFollowing = this.user.followerIds?.includes(this.currentUser.id);
  }

  toggleFollow() {
    if (!this.user || !this.currentUser) return;
    if (this.isFollowing) {
      this.userService
        .unfollowUser(this.currentUser.id, this.user.id)
        .subscribe((updatedUser) => {
          this.isFollowing = false;
          this.user = updatedUser;
        });
    } else {
      this.userService
        .followUser(this.currentUser.id, this.user.id)
        .subscribe((updatedUser) => {
          this.isFollowing = true;
          this.user = updatedUser;
        });
    }
  }

  save() {
    if (!this.user) return;
    const req: updateUserRequest = {
      userId: this.user.id,
      firstName: this.form.value.firstName,
      middleName: this.form.value.middleName,
      lastName: this.form.value.lastName,
      bio: this.form.value.bio,
      gender: this.form.value.gender,
      age: this.form.value.age,
      avatar: this.form.value.avatar,
    };

    this.userService.updateUser(req).subscribe({
      next: (updatedUser) => {
        if (this.isOwnProfile) {
          this.state.setCurrentUser(updatedUser.userDTO);
        }
        this.isEditMode = false;
        alert('Profile Updated Successfully!');
        window.location.reload();
      },
      error: (err) => {
        if (err.status == 422)
          alert('⚠️ Profile Update Failed! Bio is inappropriate');
        else alert('⚠️ Profile Update Failed!! Please Try Again.');
      },
    });
  }

  toggleEdit() {
    this.isEditMode = !this.isEditMode;
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => (this.profileImage = reader.result);
      reader.readAsDataURL(file);
    }
  }

  private loadProfile(profileId: number) {
    this.userService.getUserById(profileId).subscribe((profileUser) => {
      this.user = profileUser;
      this.form.patchValue(profileUser);
      const avatarIndex = Number(profileUser.avatar ?? 0);
      this.profileImage = this.avatars[avatarIndex];
      this.checkOwnershipAndFollow();
    });
  }

  getAvatarUrl(): string {
    return this.avatars[this.user?.avatar ?? 0];
  }

  ngOnDestroy() {
    this.subs.forEach((s) => s.unsubscribe());
  }
}
