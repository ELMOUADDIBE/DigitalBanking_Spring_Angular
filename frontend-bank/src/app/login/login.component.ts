import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  public loginForm!: FormGroup;
  constructor(private fb: FormBuilder,
              private authService : AuthService,
              private router : Router) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: this.fb.control(''),
      password: this.fb.control('')
    });
  }

  login() {
    let username = this.loginForm.value.username;
    let password = this.loginForm.value.password;
    this.authService.login(username, password).subscribe({
      next: data => {
        this.authService.loadProfile(data);
        if (this.authService.isAdmin()) {
          this.router.navigateByUrl("/admin/home");
        } else {
          this.router.navigateByUrl("/user/home");
        }
      },
      error: error => {
        console.log(error);
        Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
        }).fire({
          icon: 'error',
          title: 'Email or password incorrect'
        })
      }
    })

  }
}
