import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { JobService } from '../../core/services/job.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  constructor(private dialog: MatDialog, private jobService: JobService) {}

  ngOnInit() {}

  startScanJob() {
    this.jobService.startScanJob();
  }

  startTranscodeJob() {
    this.jobService.startTranscodeJob();
  }

  goToGitHub() {
    window.open('https://github.com/peavers/encodely', '_blank');
  }
}
