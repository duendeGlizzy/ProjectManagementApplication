import { JobStatus} from './job-status.enum';
import { JobType} from './job-type.enum';
import {Client} from './client.model';
import {PrimeContractor} from '../../contractors/models/prime-contractor.model';
import {Task} from './task.model';


export interface Job{

  jobId?: number;

  jobType: JobType;
  estimatedCost: number;
  totalPayment: number;
  address: string;
  description: string;
  status: JobStatus;

  startDate?: string | Date;
  endDate?: string | Date;

  client?: Client;
  primeContractor?: PrimeContractor;
  tasks?: Task[];



}
