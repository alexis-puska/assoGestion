import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { FileUtilsService } from 'app/shared/util/file-utils.service';
import { IConfigurationAsso } from '../configuration-asso.model';

export type EntityResponseType = HttpResponse<IConfigurationAsso>;
export type EntityArrayResponseType = HttpResponse<IConfigurationAsso[]>;

@Injectable({ providedIn: 'root' })
export class ConfigurationAssoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuration-assos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(configurationAsso: IConfigurationAsso, signature?: File): Observable<EntityResponseType> {
    const fd = new FormData();
    fd.append(
      'configurationAsso',
      new Blob([JSON.stringify(configurationAsso)], {
        type: 'application/json',
      })
    );
    if (signature) {
      fd.append('signature', signature, FileUtilsService.encodeFileName(signature.name));
    }
    return this.http.put<IConfigurationAsso>(`${this.resourceUrl}`, fd, { observe: 'response' });
  }

  getConfigurationAsso(): Observable<EntityResponseType> {
    return this.http.get<IConfigurationAsso>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
