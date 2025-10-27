import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
})

export class ErrorComponent implements OnInit {
  errorCode: string = '500';
  errorMessage: string = 'Something went wrong';
  errorDetails: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.errorCode = params['code'] || '500';
      this.errorMessage = params['message'] || 'Something went wrong';
      this.errorDetails = params['details'] || '';
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  goBack(): void {
    this.location.back();
  }

  reload(): void {
    window.location.reload();
  }
}