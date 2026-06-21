import {BillStatus} from "./bill-status.enum";
import {Task} from '../../jobs/models/task.model';
import {LineItem} from './line-item.model';
import {Vendor} from '../../contractors/models/vendor.model';
import {Job} from '../../jobs/models/job.model';

export interface Bill{

  billId?: number;

  totalAmount: number;
  description: string;


  issueDate?: string | Date;
  dueDate?: string | Date;

  billStatus: BillStatus;

  vendor?: Vendor;
  job?: Job;
  lineItems?: LineItem[];


}
