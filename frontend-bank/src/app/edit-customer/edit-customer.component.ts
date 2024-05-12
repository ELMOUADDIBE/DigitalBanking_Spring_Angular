import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { CustomerService } from "../services/customer.service";
import { Router, ActivatedRoute } from "@angular/router";
import { Customer } from "../model/customer.model";
import Swal from "sweetalert2";

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {
  editCustomerFormGroup: FormGroup;
  customer!: Customer;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.editCustomerFormGroup = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
    const customerId = +this.route.snapshot.params['id'];
    if (customerId) {
      this.customerService.getCustomer(customerId).subscribe({
        next: (customerData) => {
          this.customer = customerData;
          this.editCustomerFormGroup.patchValue({
            name: this.customer.name,
            email: this.customer.email
          });
        },
        error: (err) => {
          console.error('Failed to get customer:', err);
          this.router.navigate(['/admin/customers']);
        }
      });
    } else {
      console.error('Invalid customer ID');
      this.router.navigate(['/admin/customers']);
    }
  }

  handleUpdateCustomer(): void {
    if (this.editCustomerFormGroup.valid) {
      const updatedCustomer = { ...this.customer, ...this.editCustomerFormGroup.value };
      this.customerService.updateCustomer(updatedCustomer).subscribe({
        next: () => {
          //alert
          Swal.fire({
            title: 'Success!',
            text: 'Customer updated successfully',
            icon: 'success',
            confirmButtonText: 'OK'
          });

          this.router.navigate(['/admin/customers']);
        },
        error: (error) => {
          console.error('Error updating customer:', error);
          alert('Failed to update customer.');
        }
      });
    }
  }

  cancelEdit(): void {
    this.router.navigate(['/admin/customers']);
  }
}
