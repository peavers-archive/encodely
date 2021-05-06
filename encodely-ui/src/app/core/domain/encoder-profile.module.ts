export interface EncoderProfile {
  id?: number;
  name?: string;
  command?: string;
  extension?: string;
  dockerImage?: string;
  dockerRuntime?: string;
  active?: boolean;
  toDelete?: boolean;
}
