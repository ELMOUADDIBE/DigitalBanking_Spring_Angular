import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AccountsComponent} from "./accounts/accounts.component";
import {CustomersComponent} from "./customers/customers.component";
import {HomeComponent} from "./home/home.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {EditCustomerComponent} from "./edit-customer/edit-customer.component";

const routes: Routes = [
  { path :"home", component: HomeComponent },
  { path :"", component: HomeComponent },
  { path :"customers", component: CustomersComponent },
  { path:"accounts", component: AccountsComponent},
  { path:"new-customer", component: NewCustomerComponent},
  { path: 'edit-customer/:id', component: EditCustomerComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
