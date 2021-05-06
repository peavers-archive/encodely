import { Component, OnInit } from '@angular/core';
import { Process } from '../../../../core/domain/config.module';
import { ConfigService } from '../../../../core/services/config.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-processor',
  templateUrl: './processor.component.html',
  styleUrls: ['./processor.component.scss']
})
export class ProcessorComponent implements OnInit {
  constructor(private configService: ConfigService) {}

  formGroup: FormGroup = new FormGroup({
    deleteSource: new FormControl('', [Validators.required]),
    cleanFilename: new FormControl('', [Validators.required]),
    overwrite: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {
    this.configService.find().subscribe((config) => {
      this.formGroup.get('deleteSource')!.setValue(config.process!.deleteSource);
      this.formGroup.get('cleanFilename')!.setValue(config.process!.cleanFilename);
      this.formGroup.get('overwrite')!.setValue(config.process!.overwrite);
    });
  }

  save(input: string) {
    if (this.formGroup.get(input)?.dirty) {
      const process: Process = {
        deleteSource: this.formGroup.get('deleteSource')?.value,
        cleanFilename: this.formGroup.get('cleanFilename')?.value,
        overwrite: this.formGroup.get('overwrite')?.value
      };

      this.configService.saveProcessing(process);
      this.formGroup.get(input)?.markAsPristine({ onlySelf: true });
    }
  }
}
