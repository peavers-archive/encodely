import { Component, OnInit } from '@angular/core';
import { MediaFileService } from '../../../../core/services/media-file.service';
import { Observable } from 'rxjs';
import { JobService } from '../../../../core/services/job.service';
import { Job } from '../../../../core/domain/job.module';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {
  jobs$: Observable<Job[]> = new Observable<Job[]>();

  constructor(private mediaFileService: MediaFileService, private jobService: JobService) {}

  ngOnInit() {
    this.jobs$ = this.jobService.findAll();
  }

  deleteAll() {
    this.jobService.deleteAll();
  }

  openJobDetails(job: Job) {
    this.jobService.openJob(job);
  }
}
