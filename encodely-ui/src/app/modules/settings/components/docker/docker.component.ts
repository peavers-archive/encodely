import { Component, OnInit } from '@angular/core';
import { Docker } from '../../../../core/domain/config.module';
import { ConfigService } from '../../../../core/services/config.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-docker',
  templateUrl: './docker.component.html',
  styleUrls: ['./docker.component.scss']
})
export class DockerComponent implements OnInit {
  constructor(private configService: ConfigService) {}

  formGroup: FormGroup = new FormGroup({
    socatCommand: new FormControl('', [Validators.required]),
    containerPrefix: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {
    this.configService.find().subscribe((config) => {
      this.formGroup.get('socatCommand')!.setValue(config.docker!.socatCommand);
      this.formGroup.get('containerPrefix')!.setValue(config.docker!.containerPrefix);
    });
  }

  save(input: string) {
    if (this.formGroup.get(input)?.dirty) {
      const docker: Docker = {
        socatCommand: this.formGroup.get('socatCommand')?.value,
        containerPrefix: this.formGroup.get('socatCommand')?.value
      };

      this.configService.saveDocker(docker);
      this.formGroup.get(input)?.markAsPristine({ onlySelf: true });
    }
  }
}
