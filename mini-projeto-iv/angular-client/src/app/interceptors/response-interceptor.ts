import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpResponse }
  from '@angular/common/http';

import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators'
import {ApiResponseService} from "../services/api-response.service";

@Injectable()
export class ResponseInterceptor implements HttpInterceptor {

  constructor(
    private apiResponseService: ApiResponseService,
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    return next.handle(req).pipe(
      tap(evt => {
      if (evt instanceof HttpResponse) {
        this.apiResponseService.setApiResponse(
          `-- incerceptor --<br/>
> Status: ${evt.status} - ${evt.statusText} ---<br/>
> Corpo da resposta:<br/><br/>
${evt.body}`
        )
      }
    }));

  }
}
