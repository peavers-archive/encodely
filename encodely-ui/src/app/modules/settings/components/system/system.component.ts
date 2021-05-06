import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../../../../core/services/config.service';

@Component({
  selector: 'app-system',
  templateUrl: './system.component.html',
  styleUrls: ['./system.component.scss']
})
export class SystemComponent implements OnInit {
  constructor(private configService: ConfigService) {}

  ngOnInit() {}

  restart() {
    this.configService.restart();
  }
}
