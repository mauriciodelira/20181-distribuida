import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs/internal/BehaviorSubject";

@Injectable({
  providedIn: 'root'
})
export class ApiResponseService {
  private apiResponseSource: BehaviorSubject<string> = new BehaviorSubject(undefined);

  apiResponse = this.apiResponseSource.asObservable();

  setApiResponse(apiResponse: string) {
    this.apiResponseSource.next(apiResponse);
  }
}
