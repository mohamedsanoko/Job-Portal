import { User } from './user.model';

export type JobStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

export interface Job {
  id: number;
  title: string;
  description: string;
  location: string;
  salary?: string;
  requirements?: string;
  status: JobStatus;
  employer?: User;
  createdAt?: string;
}