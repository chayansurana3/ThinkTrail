import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WriteComponent } from './write.component';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    CommonModule,
    WriteComponent,
    RouterModule.forChild([{ path: 'write', component: WriteComponent }])
  ]
})
export class HomeModule {}