import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Calendar } from './calendar.model';

@Injectable({ providedIn: 'root' })
export class CalendarService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/calendar');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getEvent(login: string, start: Date, end: Date): Observable<HttpResponse<Calendar>> {
    let queryParam = new HttpParams();
    queryParam = queryParam.append('start', start.toISOString());
    queryParam = queryParam.append('end', end.toISOString());
    queryParam = queryParam.append('login', login);
    return this.http.get<Calendar>(`${this.resourceUrl}`, { observe: 'response', params: queryParam });
  }
}
