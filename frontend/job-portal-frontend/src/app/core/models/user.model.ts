export type UserRole = 'JOB_SEEKER' | 'EMPLOYER' | 'ADMIN';

export interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
  active?: boolean;
  token?: string;
}