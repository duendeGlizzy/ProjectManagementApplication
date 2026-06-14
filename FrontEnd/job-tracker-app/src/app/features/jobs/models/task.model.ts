import {TaskStatus} from './task-status.enum';
import {Job} from './job.model';
import {SubContractor} from '../../contractors/models/sub-contractor.model'
import {Bill} from "../../financials/services/bill";
import {Payment} from '../../financials/services/payment';


export interface Task{

  taskId?: number;

  isContracted: boolean;
  totalPrice: number;
  description: string;
  payRoll: number;

  taskStatus?: TaskStatus;

  startDate?: string | Date;
  endDate?: string | Date;


  subContractor?: SubContractor;
  job?: Job;
  payments?: Payment[];
  bills?: Bill[];

}
