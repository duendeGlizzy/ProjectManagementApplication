import {Task} from '../../jobs/models/task.model';

export interface SubContractor {

  subContractorId?: number;

  companyName: string;
  phoneNumber: string;
  price: number;

  tasks?: Task[];


}
