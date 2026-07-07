import {Employee} from './employee-model';

export interface Licence {

  licenseId: number;

  name: string;

  issueDate?: string | Date;
  expirationDate?: string | Date;

  employee?: Employee;

}
