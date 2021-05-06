import { Pipe, PipeTransform } from '@angular/core';
import { Job } from '../../core/domain/job.module';

@Pipe({ name: 'prettyJson' })
export class PrettyJsonPipe implements PipeTransform {
  transform(input: Job) {
    // Deep copy the input value as to not modify the original.
    let copiedInput = JSON.parse(JSON.stringify(input));

    // Create real version of the output data, comes from server as a JSON string
    copiedInput.output = JSON.parse(copiedInput.output);

    return JSON.stringify(copiedInput, (key, value) => this.replacer(key, value), 2);
  }

  /**
   * Hide null values from the JSON output
   */
  replacer(key: any, value: any) {
    if (value === null) {
      return undefined;
    }
    return value;
  }
}
