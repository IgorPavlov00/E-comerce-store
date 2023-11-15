import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRegister } from '../UserRegister';
import { UserRegisterService } from '../user-register.service';

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

  user: UserRegister = new UserRegister();

  constructor(
    private userRegisterService: UserRegisterService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  register() {
    this.userRegisterService.registerUser(this.user).subscribe(
      response => console.log(response),
      error => console.error(error)
    );
    this.router.navigate(['/verify']);
    this.showModal = true; // Show the modal after registration
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
}
