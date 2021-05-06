import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Job } from '../../../../core/domain/job.module';

export interface JobOutputDialogData {
  job: Job;
}

@Component({
  selector: 'app-job-output-dialog',
  templateUrl: './job-output-dialog.component.html',
  styleUrls: ['./job-output-dialog.component.scss']
})
export class JobOutputDialogComponent {
  job: Job;

  constructor(private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) private data: JobOutputDialogComponent) {
    this.job = data.job;
  }

  onCancel() {
    this.dialogRef.close();
  }

  jsonDecode() {
    return JSON.stringify(this.job, null, 2);
  }
}
