import {Job} from '../../jobs/models/job.model';


export interface PrimeContractor{

  primeContractorId?: number;

  companyName: string;
  address: string;
  phoneNumber: string;

  jobs: Job[];

}
