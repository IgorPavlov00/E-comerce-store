import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {VerifyService} from "../verify.service";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private verifyService: VerifyService) {

    this.route.queryParamMap.subscribe((queryParams) => {
      const token = queryParams.get('token');
      verifyService.verify(token).subscribe();
      console.log(verifyService.verify(token))
    });


  }

  ngOnInit(): void {

  }
}
