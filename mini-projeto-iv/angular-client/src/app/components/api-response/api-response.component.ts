import {Component, OnInit} from '@angular/core';
import {ApiResponseService} from "../../services/api-response.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-api-response',
  templateUrl: './api-response.component.html',
  styleUrls: ['./api-response.component.css']
})
export class ApiResponseComponent implements OnInit {

  response: string;

  constructor(
    private apiResponseService: ApiResponseService,
  ) {
  }

  ngOnInit() {
    this.apiResponseService.apiResponse.subscribe(resp => {
      this.response = resp;
      if (resp && resp.length > 0) alert('Confira a resposta HTTP ao lado.');
    },
      err => {
      const httpErr: HttpErrorResponse = err;
      })
  }

}
