import { Component, OnDestroy, OnInit } from '@angular/core';
import { MediaFileService } from '../../../../core/services/media-file.service';
import { Observable } from 'rxjs';
import { JobService } from '../../../../core/services/job.service';
import { MediaFile } from '../../../../core/domain/media-file.module';

@Component({
  selector: 'app-media-files',
  templateUrl: './media-files.component.html',
  styleUrls: ['./media-files.component.scss']
})
export class MediaFilesComponent implements OnInit {
  mediaFiles$: Observable<MediaFile[]> = new Observable<MediaFile[]>();

  constructor(private mediaFileService: MediaFileService, private jobService: JobService) {}

  ngOnInit() {
    this.mediaFiles$ = this.mediaFileService.findAll();
  }

  startScanJob() {
    this.jobService.startScanJob();
  }
}
