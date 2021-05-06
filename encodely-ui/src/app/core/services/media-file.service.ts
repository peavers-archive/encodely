import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { MediaFile } from '../domain/media-file.module';
import { WebsocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class MediaFileService {
  private readonly endpoint;

  private mediaFiles$: BehaviorSubject<MediaFile[]> = new BehaviorSubject<MediaFile[]>([]);

  constructor(private httpClient: HttpClient, private websockets: WebsocketService) {
    this.endpoint = `${environment.api}/media`;

    this.httpClient.get<MediaFile[]>(`${this.endpoint}/`).subscribe((mediaFiles: MediaFile[]) => this.mediaFiles$.next(mediaFiles));

    websockets.listen('mediaFiles').subscribe((message) => {
      this.mediaFiles$.next(JSON.parse(message.body));
    });
  }

  findAll(): Observable<MediaFile[]> {
    return this.mediaFiles$.asObservable();
  }
}
