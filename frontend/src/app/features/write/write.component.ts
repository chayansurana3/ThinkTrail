import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  ValidationErrors,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PostService } from '../../services/post.service';
import { StateService } from '../../services/state.service';
import { createPostRequest } from '../../requests/createPostRequest.model';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-write',
  templateUrl: './write.component.html',
  imports: [CommonModule, ReactiveFormsModule],
})
export class WriteComponent {
  postForm: FormGroup;
  private userId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private state: StateService,
    private router: Router
  ) {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      summary: ['', [Validators.required, Validators.maxLength(500)]],
      imageUrl: ['', [this.urlValidator]], // ✅ Optional now
      content: ['', [Validators.required, Validators.minLength(300)]],
    });

    this.state.currentUser$.subscribe((user) => {
      this.userId = user ? user.id : null;
    });
  }

  onSubmit() {
    if (this.postForm.valid && this.userId !== null) {
      const req: createPostRequest = {
        title: this.postForm.value.title,
        summary: this.postForm.value.summary,
        imageUrl: this.postForm.value.imageUrl || '',
        content: this.postForm.value.content,
        userId: this.userId,
      };

      this.postService.createPost(req).subscribe({
        next: (res) => {
          if (res.success) {
            alert('✅ Post Created Successfully!!');
            this.router.navigate(['/posts']);
          } else {
            alert('⚠️ Post blocked: Inappropriate content detected');
          }
        },
        error: (err) => {
          if (err.status === 422) {
            alert('⚠️ Post blocked: Inappropriate content detected');
          } else {
            console.error('Error creating post:', err);
            alert('❌ Failed to create post. Please try again.');
          }
        },
      });
    } else {
      this.postForm.markAllAsTouched();
      this.showValidationErrors();
    }
  }

  private showValidationErrors(): void {
    const controls = this.postForm.controls;

    if (controls['title'].hasError('required'))
      alert('❌ Title is required');
    else if (controls['title'].hasError('minlength'))
      alert('❌ Title must be at least 5 characters long');

    else if (controls['summary'].hasError('required'))
      alert('❌ Summary is required');
    else if (controls['summary'].hasError('maxlength'))
      alert('❌ Summary cannot exceed 500 characters');

    else if (controls['imageUrl'].hasError('invalidUrl'))
      alert('❌ Invalid Image URL format');

    else if (controls['content'].hasError('required'))
      alert('❌ Content is required');
    else if (controls['content'].hasError('minlength'))
      alert('❌ Content must be at least 300 characters long');
    else if (this.userId === null)
      alert('⚠️ User not logged in');
    else
      alert('⚠️ Invalid form, please review inputs');
  }

  urlValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null; // Skip empty values
    try {
      new URL(control.value);
      return null;
    } catch {
      return { invalidUrl: true };
    }
  }
}