import { Injectable } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { IMessage } from '@stomp/stompjs';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  constructor(private rxStompService: RxStompService) {}

  listen(topic: string): Observable<IMessage> {
    return this.rxStompService.watch(`/topic/${topic}`);
  }
}
