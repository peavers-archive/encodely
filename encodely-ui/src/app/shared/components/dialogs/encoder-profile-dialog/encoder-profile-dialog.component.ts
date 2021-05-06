import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EncoderProfile } from '../../../../core/domain/encoder-profile.module';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Select2OptionData } from 'ng-select2';

export interface EncoderProfileDialogData {
  encoderProfile: EncoderProfile;
  images: string[];
}

@Component({
  selector: 'app-encoder-profile-dialog',
  templateUrl: './encoder-profile-dialog.component.html',
  styleUrls: ['./encoder-profile-dialog.component.scss']
})
export class EncoderProfileDialogComponent implements OnInit {
  select2OptionData: Array<Select2OptionData> = [];

  encoderProfile: EncoderProfile;

  formGroup: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    command: new FormControl('', [Validators.required]),
    extension: new FormControl('', [Validators.required]),
    dockerImage: new FormControl('', [Validators.required]),
    dockerRuntime: new FormControl('', [Validators.required])
  });

  constructor(private dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) private data: EncoderProfileDialogData) {
    this.encoderProfile = data.encoderProfile;
    this.mapToSelect2(data.images);
  }

  ngOnInit(): void {
    this.formGroup.get('name')!.setValue(this.encoderProfile.name);
    this.formGroup.get('command')!.setValue(this.encoderProfile.command);
    this.formGroup.get('extension')!.setValue(this.encoderProfile.extension);
    this.formGroup.get('dockerImage')!.setValue(this.encoderProfile.dockerImage);
    this.formGroup.get('dockerRuntime')!.setValue(this.encoderProfile.dockerRuntime);
  }

  onSave() {
    this.encoderProfile.name = this.formGroup.get('name')?.value;
    this.encoderProfile.command = this.formGroup.get('command')?.value;
    this.encoderProfile.extension = this.formGroup.get('extension')?.value;
    this.encoderProfile.dockerImage = this.formGroup.get('dockerImage')?.value;
    this.encoderProfile.dockerRuntime = this.formGroup.get('dockerRuntime')?.value;

    this.dialogRef.close(this.encoderProfile);
  }

  onCancel() {
    this.dialogRef.close();
  }

  onChangeDatasource($event: any) {
    this.formGroup.patchValue({
      dockerImage: $event.value
    });
  }

  mapToSelect2(images: string[]) {
    images.forEach((image) => {
      this.select2OptionData.push({
        id: image,
        text: image
      });
    });
  }

  deleteEncoderProfile() {
    this.encoderProfile.toDelete = true;
    this.dialogRef.close(this.encoderProfile);
  }
}
