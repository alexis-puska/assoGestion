import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IHome } from './home.model';

@Injectable({ providedIn: 'root' })
export class HomeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/home');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getCounter(): Observable<HttpResponse<IHome>> {
    return this.http.get<IHome>(`${this.resourceUrl}/count`, { observe: 'response' });
  }
}
