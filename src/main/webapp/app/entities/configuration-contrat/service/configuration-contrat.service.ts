import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfigurationContrat, getConfigurationContratIdentifier } from '../configuration-contrat.model';

export type EntityResponseType = HttpResponse<IConfigurationContrat>;
export type EntityArrayResponseType = HttpResponse<IConfigurationContrat[]>;

@Injectable({ providedIn: 'root' })
export class ConfigurationContratService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuration-contrats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(configurationContrat: IConfigurationContrat): Observable<EntityResponseType> {
    return this.http.post<IConfigurationContrat>(this.resourceUrl, configurationContrat, { observe: 'response' });
  }

  update(configurationContrat: IConfigurationContrat): Observable<EntityResponseType> {
    return this.http.put<IConfigurationContrat>(
      `${this.resourceUrl}/${getConfigurationContratIdentifier(configurationContrat) as number}`,
      configurationContrat,
      { observe: 'response' }
    );
  }

  partialUpdate(configurationContrat: IConfigurationContrat): Observable<EntityResponseType> {
    return this.http.patch<IConfigurationContrat>(
      `${this.resourceUrl}/${getConfigurationContratIdentifier(configurationContrat) as number}`,
      configurationContrat,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigurationContrat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigurationContrat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConfigurationContratToCollectionIfMissing(
    configurationContratCollection: IConfigurationContrat[],
    ...configurationContratsToCheck: (IConfigurationContrat | null | undefined)[]
  ): IConfigurationContrat[] {
    const configurationContrats: IConfigurationContrat[] = configurationContratsToCheck.filter(isPresent);
    if (configurationContrats.length > 0) {
      const configurationContratCollectionIdentifiers = configurationContratCollection.map(
        configurationContratItem => getConfigurationContratIdentifier(configurationContratItem)!
      );
      const configurationContratsToAdd = configurationContrats.filter(configurationContratItem => {
        const configurationContratIdentifier = getConfigurationContratIdentifier(configurationContratItem);
        if (configurationContratIdentifier == null || configurationContratCollectionIdentifiers.includes(configurationContratIdentifier)) {
          return false;
        }
        configurationContratCollectionIdentifiers.push(configurationContratIdentifier);
        return true;
      });
      return [...configurationContratsToAdd, ...configurationContratCollection];
    }
    return configurationContratCollection;
  }
}
