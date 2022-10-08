import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { FileUtilsService } from 'app/shared/util/file-utils.service';
import { IConfigurationDon } from '../configuration-don.model';

export type EntityResponseType = HttpResponse<IConfigurationDon>;
export type EntityArrayResponseType = HttpResponse<IConfigurationDon[]>;

@Injectable({ providedIn: 'root' })
export class ConfigurationDonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuration-dons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(configurationDon: IConfigurationDon, signature?: File): Observable<EntityResponseType> {
    const fd = new FormData();
    fd.append(
      'configurationDon',
      new Blob([JSON.stringify(configurationDon)], {
        type: 'application/json',
      })
    );
    if (signature) {
      fd.append('signature', signature, FileUtilsService.encodeFileName(signature.name));
    }
    return this.http.put<IConfigurationDon>(`${this.resourceUrl}`, fd, { observe: 'response' });
  }

  getConfigurationDon(): Observable<EntityResponseType> {
    return this.http.get<IConfigurationDon>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
