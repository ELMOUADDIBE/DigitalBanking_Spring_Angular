import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  host: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.host+'/customers');
  }

  public getCustomer(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.host}/customers/${id}`);
  }

  public searchCustomers(name: string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.host+`/customers/search?keyword=${name}`);
  }

  public saveCustomer(customer: Customer):Observable<Customer>{
    return this.http.post<Customer>(this.host+'/customers', customer);
  }

  public deleteCustomer(id: number){
    return this.http.delete(this.host+"/customers/"+id);
  }

  public updateCustomer(customer: Customer):Observable<Customer>{
    return this.http.put<Customer>(this.host+`/customers/${customer.id}`, customer);
  }
}
