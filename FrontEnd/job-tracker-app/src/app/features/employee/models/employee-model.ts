import {Licence} from './licence-model';

export interface Employee {

  employeeId: number;

  firstName: string;
  lastName: string;

  email: string;
  password: string;

  licences?: Licence[];

}
