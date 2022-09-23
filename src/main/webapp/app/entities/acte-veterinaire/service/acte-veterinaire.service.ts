import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActeVeterinaire, getActeVeterinaireIdentifier } from '../acte-veterinaire.model';

export type EntityResponseType = HttpResponse<IActeVeterinaire>;
export type EntityArrayResponseType = HttpResponse<IActeVeterinaire[]>;

@Injectable({ providedIn: 'root' })
export class ActeVeterinaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acte-veterinaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acteVeterinaire: IActeVeterinaire): Observable<EntityResponseType> {
    return this.http.post<IActeVeterinaire>(this.resourceUrl, acteVeterinaire, { observe: 'response' });
  }

  update(acteVeterinaire: IActeVeterinaire): Observable<EntityResponseType> {
    return this.http.put<IActeVeterinaire>(
      `${this.resourceUrl}/${getActeVeterinaireIdentifier(acteVeterinaire) as number}`,
      acteVeterinaire,
      { observe: 'response' }
    );
  }

  partialUpdate(acteVeterinaire: IActeVeterinaire): Observable<EntityResponseType> {
    return this.http.patch<IActeVeterinaire>(
      `${this.resourceUrl}/${getActeVeterinaireIdentifier(acteVeterinaire) as number}`,
      acteVeterinaire,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActeVeterinaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActeVeterinaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActeVeterinaireToCollectionIfMissing(
    acteVeterinaireCollection: IActeVeterinaire[],
    ...acteVeterinairesToCheck: (IActeVeterinaire | null | undefined)[]
  ): IActeVeterinaire[] {
    const acteVeterinaires: IActeVeterinaire[] = acteVeterinairesToCheck.filter(isPresent);
    if (acteVeterinaires.length > 0) {
      const acteVeterinaireCollectionIdentifiers = acteVeterinaireCollection.map(
        acteVeterinaireItem => getActeVeterinaireIdentifier(acteVeterinaireItem)!
      );
      const acteVeterinairesToAdd = acteVeterinaires.filter(acteVeterinaireItem => {
        const acteVeterinaireIdentifier = getActeVeterinaireIdentifier(acteVeterinaireItem);
        if (acteVeterinaireIdentifier == null || acteVeterinaireCollectionIdentifiers.includes(acteVeterinaireIdentifier)) {
          return false;
        }
        acteVeterinaireCollectionIdentifiers.push(acteVeterinaireIdentifier);
        return true;
      });
      return [...acteVeterinairesToAdd, ...acteVeterinaireCollection];
    }
    return acteVeterinaireCollection;
  }
}
