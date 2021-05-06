import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared';
import { RoutingModule } from './settings.routing';
import { MomentModule } from 'ngx-moment';
import { SystemComponent } from './components/system/system.component';
import { NgSelect2Module } from 'ng-select2';
import { DirectoriesComponent } from './components/directories/directories.component';
import { DockerComponent } from './components/docker/docker.component';
import { EncoderProfilesComponent } from './components/encoder-profiles/encoder-profiles.component';
import { ProcessorComponent } from './components/processor/processor.component';
import { DefaultComponent } from './pages/default/default.component';

@NgModule({
  declarations: [DefaultComponent, DirectoriesComponent, DockerComponent, EncoderProfilesComponent, ProcessorComponent, SystemComponent],
  imports: [SharedModule, RoutingModule, MomentModule, MomentModule, NgSelect2Module],
  exports: [],
  entryComponents: []
})
export class SettingsModule {}
