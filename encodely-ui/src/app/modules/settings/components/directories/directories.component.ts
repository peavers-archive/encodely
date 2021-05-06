import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../../../../core/services/config.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Scan } from '../../../../core/domain/config.module';

@Component({
  selector: 'app-directories',
  templateUrl: './directories.component.html',
  styleUrls: ['./directories.component.scss']
})
export class DirectoriesComponent implements OnInit {
  constructor(private configService: ConfigService) {}

  formGroup: FormGroup = new FormGroup({
    input: new FormControl('', [Validators.required]),
    output: new FormControl('', [Validators.required]),
    exclude: new FormControl('', [Validators.required]),
    minFileSize: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {
    this.configService.find().subscribe((config) => {
      this.formGroup.get('input')!.setValue(config.scan!.input);
      this.formGroup.get('output')!.setValue(config.scan!.output);
      this.formGroup.get('exclude')!.setValue(config.scan!.exclude);
      this.formGroup.get('minFileSize')!.setValue(config.scan!.minFileSize);
    });
  }

  save(input: string) {
    if (this.formGroup.get(input)?.dirty) {
      const scan: Scan = {
        input: this.formGroup.get('input')?.value,
        output: this.formGroup.get('output')?.value,
        minFileSize: this.formGroup.get('minFileSize')?.value,
        exclude: this.formGroup.get('exclude')?.value
      };

      this.configService.saveScan(scan);
      this.formGroup.get(input)?.markAsPristine({ onlySelf: true });
    }
  }
}
