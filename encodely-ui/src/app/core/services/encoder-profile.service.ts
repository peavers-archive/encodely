import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { EncoderProfile } from '../domain/encoder-profile.module';
import {
  EncoderProfileDialogComponent,
  EncoderProfileDialogData
} from '../../shared/components/dialogs/encoder-profile-dialog/encoder-profile-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class EncoderProfileService {
  private readonly endpoint;

  encoderProfiles$: BehaviorSubject<EncoderProfile[]> = new BehaviorSubject<EncoderProfile[]>([]);

  constructor(
    private httpClient: HttpClient,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private configService: ConfigService
  ) {
    this.endpoint = `${environment.api}/encoder-profile`;

    this.httpClient.get<EncoderProfile[]>(`${this.endpoint}/`).subscribe((encoderProfiles) => {
      this.encoderProfiles$.next(encoderProfiles);
    });
  }

  save(encoderProfile: EncoderProfile): void {
    this.httpClient.post<EncoderProfile>(`${this.endpoint}/`, encoderProfile).subscribe((encoderProfile) => {
      if (this.encoderProfiles$.value.find((f) => f.id === encoderProfile.id) === undefined) {
        this.encoderProfiles$.next(this.encoderProfiles$.value.concat(encoderProfile));
      }

      this.snackBar.open('Successfully saved profile', undefined, {
        duration: 1000
      });
    });
  }

  findAll(): Observable<EncoderProfile[]> {
    return this.encoderProfiles$.asObservable();
  }

  delete(id: number | undefined) {
    this.httpClient.delete<void>(`${this.endpoint}/${id}`).subscribe(() => {
      this.encoderProfiles$.next(this.encoderProfiles$.value.filter((f) => f.id !== id));

      this.snackBar.open('Successfully deleted profile', undefined, {
        duration: 1000
      });
    });
  }

  setDefaultEncoderProfile(encoderProfile: EncoderProfile) {
    this.httpClient.post(`${this.endpoint}/default`, encoderProfile).subscribe(() => {
      this.snackBar.open('Saved new default encoder profile', undefined, {
        duration: 1000
      });
    });
  }

  createNewEncoderProfile() {
    this.configService.getImages().subscribe((images: string[]) => {
      const data: EncoderProfileDialogData = {
        encoderProfile: {},
        images: images
      };

      this.dialog
        .open(EncoderProfileDialogComponent, { width: '50vw', data: data })
        .afterClosed()
        .subscribe((encoderProfile) => {
          if (encoderProfile != undefined) {
            this.save(encoderProfile);
          }
        });
    });
  }

  editEncoderProfile(encoderProfile: EncoderProfile) {
    this.configService.getImages().subscribe((images: string[]) => {
      const data: EncoderProfileDialogData = {
        encoderProfile: encoderProfile,
        images: images
      };

      this.dialog
        .open(EncoderProfileDialogComponent, { width: '50vw', data: data })
        .afterClosed()
        .subscribe((encoderProfile) => {
          if (encoderProfile != undefined) {
            if (encoderProfile.toDelete) {
              this.delete(encoderProfile.id);
            } else {
              this.save(encoderProfile);
            }
          }
        });
    });
  }
}
