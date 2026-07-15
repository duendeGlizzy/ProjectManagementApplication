import { Task } from "../../jobs/models/task.model";
import {PaymentMethod} from "./payment-method.enum";
import {Job} from '../../jobs/models/job.model';


export interface Payment {

 paymentId?: number;

 checkAmount: number;
 dateReceived: string | Date;

 paymentMethod: PaymentMethod;
 referenceNumber: string;

 checkImageKey?: string;

 job?: Job;


}
