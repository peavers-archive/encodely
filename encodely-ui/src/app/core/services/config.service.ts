import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Config, Docker, Process, Scan } from '../domain/config.module';
import { ConfirmDialogComponent } from '../../shared/components/dialogs/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private readonly endpoint;

  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar, private dialog: MatDialog) {
    this.endpoint = `${environment.api}/config`;
  }

  saveDocker(docker: Docker): void {
    this.httpClient.post<Config>(`${this.endpoint}/docker`, docker).subscribe(() => {
      this.snackBar.open('Successfully updated docker settings', undefined, {
        duration: 1000
      });
    });
  }

  saveProcessing(process: Process): void {
    this.httpClient.post<Config>(`${this.endpoint}/process`, process).subscribe(() => {
      this.snackBar.open('Successfully updated process settings', undefined, {
        duration: 1000
      });
    });
  }

  saveScan(scan: Scan): void {
    this.httpClient.post<Config>(`${this.endpoint}/scan`, scan).subscribe(() => {
      this.snackBar.open('Successfully updated directory settings', undefined, {
        duration: 1000
      });
    });
  }

  save(config: Config): void {
    this.httpClient.post<Config>(`${this.endpoint}/`, config).subscribe(() => {
      this.snackBar.open('Successfully updated settings', undefined, {
        duration: 1000
      });
    });
  }

  restart() {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title: 'Restart',
          message: "Should only be needed if you've made changes to the SOCAT command."
        }
      })
      .afterClosed()
      .subscribe((confirm: boolean) => {
        if (confirm) {
          this.httpClient.get<Config>(`${this.endpoint}/restart`).subscribe(() => {
            this.snackBar.open('Restarting...', undefined, {
              duration: 1000
            });
          });
        }
      });
  }

  find(): Observable<Config> {
    return this.httpClient.get<Config>(`${this.endpoint}/`);
  }

  getImages(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.endpoint}/images`);
  }
}
