export interface MediaFile {
  id: string;
  name: string;
  path: string;
  extension: string;
  size: number;
  processed: string;
  deleted: string;

  transcodeResult: TranscodeResult;
}

export interface TranscodeResult {
  id: number;
  exitCode: number;
  ffmpegCommand: string;
  stdout: string;
  stderr: string;
  outputFilename: string;
  outputPath: string;
}
