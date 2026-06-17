import {Bill} from '../../financials/models/bill.model';

export interface Vendor {

  vendorId?: number;

  companyName: string;
  description: string;
  address: string;
  phoneNumber: string;

  bills?: Bill[];

}
