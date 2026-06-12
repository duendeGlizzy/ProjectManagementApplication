import { Task } from "../../jobs/models/task.model";
import {PaymentMethod} from "./payment-method.enum";


export interface Payment {

 paymentId?: number;

 checkAmount: number;
 dateReceived: string | Date;

 paymentMethod: PaymentMethod;

 task?: Task;


}
