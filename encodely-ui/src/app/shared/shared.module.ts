import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MaterialModule } from './material.module';
import { FileSizePipe } from './pipes/file-size.pipe';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ConfirmDialogComponent } from './components/dialogs/confirm-dialog/confirm-dialog.component';
import { EncoderProfileDialogComponent } from './components/dialogs/encoder-profile-dialog/encoder-profile-dialog.component';
import { StartTranscodeJobDialogComponent } from './components/dialogs/start-transcode-job-dialog/start-transcode-job-dialog.component';
import { JobOutputDialogComponent } from './components/dialogs/job-output-dialog/job-output-dialog.component';
import { PrettyJsonPipe } from './pipes/pretty-json.pipe';
import { JobStatusIconComponent } from './components/job-status-icon/job-status-icon.component';
import { MomentModule } from 'ngx-moment';
import { NgSelect2Module } from 'ng-select2';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatProgressSpinnerModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
    MomentModule,
    NgSelect2Module
  ],
  declarations: [
    FileSizePipe,
    PrettyJsonPipe,
    ConfirmDialogComponent,
    EncoderProfileDialogComponent,
    StartTranscodeJobDialogComponent,
    JobOutputDialogComponent,
    JobStatusIconComponent
  ],
  exports: [
    CommonModule,
    FileSizePipe,
    PrettyJsonPipe,
    FormsModule,
    HttpClientModule,
    MaterialModule,
    ReactiveFormsModule,
    RouterModule,
    JobStatusIconComponent
  ],
  entryComponents: [ConfirmDialogComponent, EncoderProfileDialogComponent, StartTranscodeJobDialogComponent, JobOutputDialogComponent],
  providers: []
})
export class SharedModule {}
