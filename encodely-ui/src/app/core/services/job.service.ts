import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { Job } from '../domain/job.module';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebsocketService } from './websocket.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/components/dialogs/confirm-dialog/confirm-dialog.component';
import { StartTranscodeJobDialogComponent } from '../../shared/components/dialogs/start-transcode-job-dialog/start-transcode-job-dialog.component';
import { JobOutputDialogComponent } from '../../shared/components/dialogs/job-output-dialog/job-output-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private readonly endpoint;

  private jobs$: BehaviorSubject<Job[]> = new BehaviorSubject<Job[]>([]);

  private jobsRunning$: BehaviorSubject<Job[]> = new BehaviorSubject<Job[]>([]);

  constructor(
    private httpClient: HttpClient,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private websockets: WebsocketService
  ) {
    this.endpoint = `${environment.api}/job`;

    this.httpClient.get<Job[]>(`${this.endpoint}`).subscribe((job: Job[]) => this.jobs$.next(job));
    this.httpClient.get<Job[]>(`${this.endpoint}/running`).subscribe((job: Job[]) => this.jobsRunning$.next(job));

    websockets.listen('job').subscribe((message) => {
      const job: Job = JSON.parse(message.body);

      this.updateSubject(this.jobs$, job);
      this.updateSubject(this.jobsRunning$, job);
    });
  }

  findAll(): Observable<Job[]> {
    return this.jobs$.asObservable();
  }

  findRunning(): Observable<Job[]> {
    return this.jobsRunning$.asObservable();
  }

  openJob(job: Job) {
    this.dialog.open(JobOutputDialogComponent, {
      width: '50vw',
      data: {
        job: job
      }
    });
  }

  deleteAll(): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Clear history table',
          message: 'Delete history of all jobs'
        }
      })
      .afterClosed()
      .subscribe((confirm) => {
        if (confirm) {
          this.httpClient.delete<void>(`${this.endpoint}`).subscribe(() => {
            this.jobs$.next([]);
            this.jobsRunning$.next([]);

            this.snackBar.open('Clearing job history...', undefined, {
              duration: 1000
            });
          });
        }
      });
  }

  stopJob(job: Job): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Stop job',
          message: 'Will abort the job and stop any related docker container.'
        }
      })
      .afterClosed()
      .subscribe((confirm) => {
        if (confirm) {
          this.httpClient.delete<void>(`${this.endpoint}/${job.id}`).subscribe(() => {
            const data = this.jobsRunning$.value.filter((j) => j.id !== job.id);
            this.jobsRunning$.next(data);

            this.snackBar.open('Job stopped...', undefined, {
              duration: 1000
            });
          });
        }
      });
  }

  startScanJob(): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Start scan job',
          message: 'This will scan your input directory for all media files. Depending on the number of files it could take a minute.'
        }
      })
      .afterClosed()
      .subscribe((confirm) => {
        if (confirm) {
          this.httpClient.post<Job[]>(`${this.endpoint}/scan-job`, null).subscribe((result: Job[]) => {
            this.jobs$.next(result.concat(this.jobs$.value));

            this.snackBar.open('Starting scan job...', undefined, {
              duration: 1000
            });
          });
        }
      });
  }

  startTranscodeJob(): void {
    this.dialog
      .open(StartTranscodeJobDialogComponent)
      .afterClosed()
      .subscribe((encoderProfile) => {
        if (encoderProfile) {
          console.log(encoderProfile);

          this.httpClient.post<Job[]>(`${this.endpoint}/transcode-job`, encoderProfile).subscribe((result: Job[]) => {
            this.jobs$.next(result.concat(this.jobs$.value));

            this.snackBar.open('Starting transcode job...', undefined, {
              duration: 1000
            });
          });
        }
      });
  }

  private updateSubject(input: BehaviorSubject<any>, job: Job) {
    const index = input.value.findIndex((value: any) => value.id === job.id);

    if (index != -1) {
      const data = input.value;

      // @ts-ignore
      data.forEach((item, i) => {
        if (item.id == job.id) data[i] = job;
      });

      input.next(data);
    } else {
      input.next([job, ...input.value]);
    }
  }
}
