import {Job} from './job.model';


export interface Client{

  clientId?: number;

  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: string;

  jobs: Job[];

}
