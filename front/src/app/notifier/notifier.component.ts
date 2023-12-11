import { Component } from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-notifier',
  templateUrl: './notifier.component.html',
  styleUrls: ['./notifier.component.css']
})
export class NotifierComponent {


  constructor(private toastr:ToastrService) {
  }

  ngOnInit(){

  }

  ShowSuccess(){
    this.toastr.success('successfully registered!')
  }
}
