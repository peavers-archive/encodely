import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EncoderProfile } from '../../../../core/domain/encoder-profile.module';
import { EncoderProfileService } from '../../../../core/services/encoder-profile.service';

export interface StartTranscodeJobDialogData {
  encoderProfiles: EncoderProfile[];
}

@Component({
  selector: 'app-start-transcode-job-dialog',
  templateUrl: './start-transcode-job-dialog.component.html',
  styleUrls: ['./start-transcode-job-dialog.component.scss']
})
export class StartTranscodeJobDialogComponent {
  profile: EncoderProfile = {};

  encoderProfiles: EncoderProfile[] = [];

  constructor(
    private dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) private data: StartTranscodeJobDialogData,
    private encoderService: EncoderProfileService
  ) {
    this.encoderService.findAll().subscribe((encoders) => (this.encoderProfiles = encoders));
  }

  onCancel() {
    this.dialogRef.close();
  }

  selectionChanged($event: any) {
    this.profile = $event.value;
  }
}
