import {TaskStatus} from './task-status.enum';
import {Job} from './job.model';
import {SubContractor} from '../../contractors/models/sub-contractor.model'
import {Bill} from "../../financials/models/bill.model";
import {Payment} from '../../financials/models/payment.model';


export interface Task{

  taskId?: number;

  isSubContracted: boolean;
  totalPrice: number;
  description: string;
  payRoll: number;

  status?: TaskStatus;

  startDate?: string | Date;
  endDate?: string | Date;


  subContractor?: SubContractor;
  job?: Job;


}
