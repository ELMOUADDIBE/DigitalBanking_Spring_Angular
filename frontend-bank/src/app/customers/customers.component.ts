import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../model/customer.model";
import {Router} from "@angular/router";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{

  public customers!: any;
  public dataSource: any;
  public errorMessage!: string;
  public displayedColumns: string[] = ['id', 'name', 'email', 'actions'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  constructor(private customerService: CustomerService, private router:Router) { }

  ngOnInit(): void {
    this.customerService.getCustomers()
      .subscribe({
        next: data => {
          this.customers = data;
          this.dataSource = new MatTableDataSource<Customer>(this.customers);
          setTimeout(() => {
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          });
        },
        error: error => {
          this.errorMessage = error.message;
        }
      });
  }

  applyFilter(value: string) {
    this.dataSource.filter = value.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: Customer, filter: string) => data.name.toLowerCase().includes(filter) || data.email.toLowerCase().includes(filter);
  }
  handleDeleteCustomer(customer: Customer) {
    //alert
    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you want to delete this customer?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'

    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.deleteCustomer(customer.id).subscribe({
          next: () => {
            this.customers = this.customers.filter((c: Customer) => c.id !== customer.id);
            this.dataSource.data = this.customers;
            Swal.fire(
              'Deleted!',
              'Customer deleted successfully.',
              'success'
            );
          },
          error: error => {
            console.error('Error deleting customer:', error);
            this.errorMessage = 'Failed to delete customer: ' + (error.message || 'Unknown error');
            //alert
            Swal.fire({
              title: 'Error!',
              text: this.errorMessage,
              icon: 'error',
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }


  handleUpdateCustomer(customer: Customer) {
    this.router.navigate(['/admin/edit-customer', customer.id]);  }
}
