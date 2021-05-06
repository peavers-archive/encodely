import { Component, OnInit } from '@angular/core';
import { MediaFileService } from '../../../../core/services/media-file.service';
import { JobService } from '../../../../core/services/job.service';
import { Job } from '../../../../core/domain/job.module';
import { Observable } from 'rxjs';
import * as moment from 'moment';

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.scss']
})
export class DefaultComponent implements OnInit {
  jobs$: Observable<Job[]> = new Observable<Job[]>();

  constructor(private mediaFileService: MediaFileService, private jobService: JobService) {}

  ngOnInit(): void {
    this.jobs$ = this.jobService.findRunning();
  }

  openJobDetails(job: Job): void {
    this.jobService.openJob(job);
  }

  stopJob(job: Job): void {
    this.jobService.stopJob(job);
  }

  getDuration(job: Job): string {
    if (job.endTime) {
      const duration = moment.utc(moment(job.endTime).diff(moment(job.startTime)));

      return moment.utc(duration).format('HH:mm:ss');
    } else {
      return 'Still processing';
    }
  }
}
