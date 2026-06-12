import {Bill} from '../../financials/services/bill';

export interface Vendor {

  vendorId?: number;

  companyName: string;
  description: string;
  address: string;
  phoneNumber: string;

  bills?: Bill[];

}
