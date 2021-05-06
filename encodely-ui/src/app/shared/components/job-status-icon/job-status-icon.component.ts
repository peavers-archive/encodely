import { Component, Input } from '@angular/core';
import { Job } from '../../../core/domain/job.module';

@Component({
  selector: 'app-job-status-icon',
  templateUrl: './job-status-icon.component.html',
  styleUrls: ['./job-status-icon.component.scss']
})
export class JobStatusIconComponent {
  @Input()
  job: Job = {};

  animState = false;

  animDone(): void {
    this.animState = !this.animState;
  }
}
