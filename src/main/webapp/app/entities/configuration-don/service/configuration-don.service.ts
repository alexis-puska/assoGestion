import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfigurationDon, getConfigurationDonIdentifier } from '../configuration-don.model';

export type EntityResponseType = HttpResponse<IConfigurationDon>;
export type EntityArrayResponseType = HttpResponse<IConfigurationDon[]>;

@Injectable({ providedIn: 'root' })
export class ConfigurationDonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuration-dons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(configurationDon: IConfigurationDon): Observable<EntityResponseType> {
    return this.http.post<IConfigurationDon>(this.resourceUrl, configurationDon, { observe: 'response' });
  }

  update(configurationDon: IConfigurationDon): Observable<EntityResponseType> {
    return this.http.put<IConfigurationDon>(
      `${this.resourceUrl}/${getConfigurationDonIdentifier(configurationDon) as number}`,
      configurationDon,
      { observe: 'response' }
    );
  }

  partialUpdate(configurationDon: IConfigurationDon): Observable<EntityResponseType> {
    return this.http.patch<IConfigurationDon>(
      `${this.resourceUrl}/${getConfigurationDonIdentifier(configurationDon) as number}`,
      configurationDon,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigurationDon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigurationDon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConfigurationDonToCollectionIfMissing(
    configurationDonCollection: IConfigurationDon[],
    ...configurationDonsToCheck: (IConfigurationDon | null | undefined)[]
  ): IConfigurationDon[] {
    const configurationDons: IConfigurationDon[] = configurationDonsToCheck.filter(isPresent);
    if (configurationDons.length > 0) {
      const configurationDonCollectionIdentifiers = configurationDonCollection.map(
        configurationDonItem => getConfigurationDonIdentifier(configurationDonItem)!
      );
      const configurationDonsToAdd = configurationDons.filter(configurationDonItem => {
        const configurationDonIdentifier = getConfigurationDonIdentifier(configurationDonItem);
        if (configurationDonIdentifier == null || configurationDonCollectionIdentifiers.includes(configurationDonIdentifier)) {
          return false;
        }
        configurationDonCollectionIdentifiers.push(configurationDonIdentifier);
        return true;
      });
      return [...configurationDonsToAdd, ...configurationDonCollection];
    }
    return configurationDonCollection;
  }
}
