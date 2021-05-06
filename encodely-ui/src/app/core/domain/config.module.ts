export interface Config {
  scan?: Scan;
  docker?: Docker;
  process?: Process;
  encoder?: Encoder;
}

export interface Scan {
  input?: string;
  output?: string;
  exclude?: string;
  minFileSize?: number;
}

export interface Docker {
  socatCommand: string;
  containerPrefix: string;
}

export interface Process {
  deleteSource?: boolean;
  cleanFilename?: boolean;
  overwrite?: boolean;
}

export interface Encoder {
  name: string;
  command: string;
  extension: string;
  hardwareAccelerated: boolean;
}
