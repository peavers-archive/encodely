import { Component, OnInit } from '@angular/core';
import { MediaFileService } from '../../../../core/services/media-file.service';
import { JobService } from '../../../../core/services/job.service';
import { Job } from '../../../../core/domain/job.module';
import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { JobOutputDialogComponent } from '../../../../shared/components/dialogs/job-output-dialog/job-output-dialog.component';

@Component({
  selector: 'app-manual-job',
  templateUrl: './manual-job.component.html',
  styleUrls: ['./manual-job.component.scss']
})
export class ManualJobComponent implements OnInit {
  constructor(private dialog: MatDialog, private jobService: JobService) {}

  ngOnInit() {}

  startScanJob() {
    this.jobService.startScanJob();
  }

  startTranscodeJob() {
    this.jobService.startTranscodeJob();
  }
}
