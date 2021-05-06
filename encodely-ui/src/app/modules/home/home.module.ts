import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared';
import { DefaultComponent } from './pages/default/default.component';
import { RoutingModule } from './home.routing';
import { MediaFilesComponent } from './pages/media-files/media-files.component';
import { HistoryComponent } from './pages/history/history.component';
import { MomentModule } from 'ngx-moment';
import { NgSelect2Module } from 'ng-select2';
import { ManualJobComponent } from './pages/manual-job/manual-job.component';

@NgModule({
  declarations: [DefaultComponent, MediaFilesComponent, HistoryComponent, ManualJobComponent],
  imports: [SharedModule, RoutingModule, MomentModule, MomentModule, NgSelect2Module],
  exports: [],
  entryComponents: []
})
export class HomeModule {}
