import {Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {ApiResponseService} from "./api-response.service";

@Injectable({
  providedIn: 'root'
})
export class HttpErrorHandlerService {
  constructor(
    private apiResponse: ApiResponseService,
  ) {
  }

  handle(err: HttpErrorResponse) {
    this.apiResponse.setApiResponse(
      `> Status: ${err.status} - ${err.statusText} ---<br/> > Corpo da resposta:<br/><br/>${err.message}`);
  }

}
