import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRegister } from '../UserRegister';
import { UserRegisterService } from '../user-register.service';
import {ToastrService} from "ngx-toastr";
import { UserLogin } from '../UserLogin';

@Component({
  selector: 'app-login-register',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.css']
})
export class LoginRegisterComponent implements OnInit {
  container!: HTMLElement;
  registerBtn!: HTMLElement;
  loginBtn!: HTMLElement;

  showModal: boolean = false; // Add this line to track modal visibility
  userLog: UserLogin = new UserLogin();
  user: UserRegister = new UserRegister();
  isLoggedIn: boolean = false; // Add this line to declare the isLoggedIn property

  constructor(
    private userRegisterService: UserRegisterService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr:ToastrService,

  ) {}

  register() {
    // Check if all fields are filled
    if (!this.user.name || !this.user.lastname || !this.user.email || !this.user.password) {
      this.toastr.error('Please fill in all the fields', 'Error');
      return;
    }

    // All fields are filled, proceed with registratio
    this.toastr.success('Check your email at:'+this.user.email, 'Successful registration!');
    this.userRegisterService.registerUser(this.user).subscribe(
      response => {
        console.log(response);

      },
      error => {
        console.error(error);
        this.toastr.error('Registration failed', 'Error');
      }
    );
    // this.router.navigate(['/verify']);
    // this.showModal = true; // Show the modal after registration
  }
  signIn() {
    this.userRegisterService.login(this.userLog)
      .subscribe(
        response => {
          // Handle successful login
          console.log('Login successful');
          localStorage.setItem('isLoggedIn', 'true');
          // Update isLoggedIn variable
          this.isLoggedIn = true;
          this.router.navigate(['/']);
        },
        error => {
          // Handle login error
          console.log("error");
        }
      );
  }
  ngOnInit(): void {
    this.container = document.getElementById('container')!;
    this.registerBtn = document.getElementById('register')!;
    this.loginBtn = document.getElementById('login')!;

    this.registerBtn.addEventListener('click', () => {
      this.container.classList.add('active');
    });

    this.loginBtn.addEventListener('click', () => {
      this.container.classList.remove('active');
    });
  }



    ShowSuccess(){
      this.toastr.success('successfully registered!')
    }

}
