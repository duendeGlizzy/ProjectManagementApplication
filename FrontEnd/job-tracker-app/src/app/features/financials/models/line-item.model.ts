import {TaxCategory} from "./tax-category.enum";
import {Bill} from './bill.model';


export interface LineItem{

  lineItemId?: number;

  description: string;
  quantity: number;
  unitPrice: number;
  subTotal: number;

  taxCategory: TaxCategory;

  bill?: Bill;

}
